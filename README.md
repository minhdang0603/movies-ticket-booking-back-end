# Kafka stream processing command

## Prerequisites:

- Ensure that the Kafka, KSQL, Schema Registry, Kafka connect servers are running.
- **Kafka topic**:
  - `movie_db.audio`
  - `movie_db.movie`
  - `movie_db.city`
  - `movie_db.cinema`
  - `movie_db.cinema_image`
  - `movie_db.shows`
- **MySQL table**:
  - `audio`
  - `movie`
  - `city`
  - `cinema`
  - `cinema_image`
  - `shows`

## Open ksqlDB cli commands

```docker
docker exec -it ksqldb bash -c
'echo -e "\n\n⏳ Waiting for ksqlDB to be available before launching CLI\n";
while : ; do
    curl_status=$(curl -s -o /dev/null -w %{http_code} http://ksqldb:8088/info) ;
    echo -e $(date) " ksqlDB server listener HTTP state: " $curl_status " (waiting for 200)" ;
    if [ $curl_status -eq 200 ] ; then
        break ;
    fi ;
    sleep 5 ;
done ;
ksql http://ksqldb:8088'
```

## KSQL Command to register connection with MySQL server:
```sql
CREATE SOURCE CONNECTOR source_mysql WITH (
    'connector.class' = 'io.debezium.connector.mysql.MySqlConnector',
    'tasks.max' = '1',
    'database.hostname' = 'mysql',
    'database.port' = '3306',
    'database.user' = 'debezium',
    'database.password' = 'debezium',
    'database.server.id' = '184054',
    'topic.prefix' = 'mysql',
    'database.include.list' = 'movie_ticket_booking',
    'table.include.list' = 'movie_ticket_booking.movie,movie_ticket_booking.shows,movie_ticket_booking.cinema',
    'schema.history.internal.kafka.topic' ='schema-changes.movie_ticket_booking',
    'schema.history.internal.kafka.bootstrap.servers' = 'broker:29092',
    'transforms' = 'unwrap,reroute_topic,extractKey',

    'transforms.unwrap.type' = 'io.debezium.transforms.ExtractNewRecordState',
    'transforms.unwrap.drop.tombstones' = 'false',

    'transforms.reroute_topic.type' = 'io.debezium.transforms.ByLogicalTableRouter',
    'transforms.reroute_topic.key.enforce.uniqueness' = 'false',
    'transforms.reroute_topic.topic.regex' = '^mysql\\.movie_ticket_booking\\.(.+)$',
    'transforms.reroute_topic.topic.replacement' = 'movie_db.$1',

    'predicates' = 'topicNameMatch',
    'predicates.topicNameMatch.type' = 'org.apache.kafka.connect.transforms.predicates.TopicNameMatches',
    'predicates.topicNameMatch.pattern' = 'movie_db\\..+',

    'transforms.extractKey.type' ='org.apache.kafka.connect.transforms.ExtractField$Key',
    'transforms.extractKey.field' ='id',
    'transforms.extractKey.predicate' = 'topicNameMatch',

    'key.converter' = 'org.apache.kafka.connect.storage.StringConverter',
    'value.converter' = 'io.confluent.connect.avro.AvroConverter',
    'value.converter.schema.registry.url' = 'http://schema-registry:8081',
    'snapshot.mode' = 'initial'
)
```

## KSQL Command to create the `movie` table

```sql
CREATE table movie (
    id VARCHAR PRIMARY KEY,
    title VARCHAR,
    director VARCHAR,
    actors VARCHAR
) WITH (
    KAFKA_TOPIC='movie_db.movie',
    VALUE_FORMAT='avro',
    partitions=1
);
```

## KSQL Command to create the `movie_stream` stream

```sql
CREATE stream movie_stream_src (
    "id" VARCHAR,
    "title" VARCHAR,
    "director" VARCHAR,
    "actors" VARCHAR
) WITH (
    KAFKA_TOPIC='movie_db.movie',
    VALUE_FORMAT='avro'
);

SET 'auto.offset.reset' = 'earliest';
CREATE stream movie_stream with (
    kafka_topic='movie',
    value_format='avro'
) AS
SELECT * FROM movie_stream_src partition by "id"
EMIT CHANGES;
```

## KSQL Command to Create the `cinema` stream

```sql
create stream cinema with (
   kafka_topic='movie_db.cinema',
   value_format='avro'
);
```

## KSQL Command to modify the `cinema` stream

```sql
SET 'auto.offset.reset' = 'earliest';
create stream cinema_enriched with(
    kafka_topic='cinema',
    value_format='avro'
) as
SELECT
    c.id as "id",
    c.name as "name",
    ARRAY[CAST(c.longitude AS DOUBLE), CAST(c.latitude AS DOUBLE)] AS "location"
FROM cinema c
partition by c.id
EMIT CHANGES;
```

## KSQL Command to create the `cinema_table` table from `cinema` topic

```sql
create table cinema_table (
    id varchar primary key,
    name varchar,
    location ARRAY<DOUBLE>
) with (kafka_topic='cinema', value_format='avro', partitions=1);
```

## KSQL Command to Create the `shows` stream

```sql
CREATE STREAM shows_stream WITH (
    KAFKA_TOPIC = 'movie_db.shows',
    VALUE_FORMAT = 'AVRO'
);
```

## KSQL Command to create the `enriched_shows` stream with topic `shows`

```sql
CREATE STREAM enriched_shows with (
    kafka_topic='shows',
    value_format='avro'
) AS
SELECT
    s.id as "id",
    cast(s.price as int) as "price",
    STRUCT(
        "id":=m.id,
        "title":=m.title,
        "director":=m.director,
        "actors":=m.actors
    ) AS "movie",
    STRUCT(
        "id":=c.id,
        "cinema_name":=c.name,
        "location":=c.location
    ) AS "cinema"
FROM shows_stream s
JOIN movie m ON s.movie_id = m.id
JOIN cinema_table c ON s.cinema_id = c.id
partition by s.id
EMIT CHANGES;
```
## KSQL Command to register connection with Elasticsearch server:
```sql
CREATE SOURCE CONNECTOR source_mysql WITH (
    'type.name' = '_doc',
    'connector.client.config.override.policy' = 'All',
    'consumer.override.group.id' = 'movie_db-consumer-group',
    'consumer.override.client.id' = 'movie-connector-client',
    'name' = 'es-connector',
    'connector.class' = 'io.confluent.connect.elasticsearch.ElasticsearchSinkConnector',
    'tasks.max' = '1',
    'topics' = 'movie, cinema, shows',
    'connection.url' = 'http://elasticsearch:9200',
    'max.retries' = '2',
    'errors.tolerance' = 'all',
    'errors.log.enable' = 'true',
    'errors.log.include.messages' = 'true',
    'retry.backoff.ms' = '3000',
    'schema.ignore' = 'true',
    'schemas.enable' = 'false',
    'behavior.on.null.values' = 'DELETE',
    'key.converter' = 'org.apache.kafka.connect.storage.StringConverter'
);
```