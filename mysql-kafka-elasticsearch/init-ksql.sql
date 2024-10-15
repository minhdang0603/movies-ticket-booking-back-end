-- Connect to MySQL using Kafka Connect
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
    'transforms.reroute_topic.topic.regex' = '^mysql\\.movie_ticket_booking\\.(.+)\$',
    'transforms.reroute_topic.topic.replacement' = 'movie_db.\$1',
    'predicates' = 'topicNameMatch',
    'predicates.topicNameMatch.type' = 'org.apache.kafka.connect.transforms.predicates.TopicNameMatches',
    'predicates.topicNameMatch.pattern' = 'movie_db\\..+',
    'transforms.extractKey.type' ='org.apache.kafka.connect.transforms.ExtractField\$Key',
    'transforms.extractKey.field' ='id',
    'transforms.extractKey.predicate' = 'topicNameMatch',
    'key.converter' = 'org.apache.kafka.connect.storage.StringConverter',
    'key.converter.schema.registry.url' = 'http://schema-registry:8081',
    'value.converter' = 'org.apache.kafka.connect.json.JsonConverter',
    'value.converter.schema.registry.url' = 'http://schema-registry:8081',
    'snapshot.mode' = 'initial'
);

show connectors;

-- Create Movie table
CREATE table movie (
    id VARCHAR PRIMARY KEY,
    title VARCHAR,
    director VARCHAR,
    actors VARCHAR,
    description VARCHAR
) WITH (
    KAFKA_TOPIC='movie_db.movie',
    VALUE_FORMAT='avro',
    partitions=1
);

-- Create Movie Stream
CREATE stream movie_stream_src (
    id VARCHAR,
    title VARCHAR,
    director VARCHAR,
    actors VARCHAR,
    description VARCHAR
) WITH (
    KAFKA_TOPIC='movie_db.movie',
    VALUE_FORMAT='avro'
);

SET 'auto.offset.reset' = 'earliest';
CREATE stream movie_stream WITH (
    kafka_topic='movie',
    value_format='avro'
) AS
SELECT * FROM movie_stream_src PARTITION BY id
EMIT CHANGES;

-- Create Cinema Stream and Enrich it
CREATE stream cinema WITH (
    kafka_topic='movie_db.cinema',
    value_format='avro'
);

SET 'auto.offset.reset' = 'earliest';
CREATE stream cinema_enriched WITH (
    kafka_topic='cinema',
    value_format='avro'
) AS
SELECT
    c.id,
    c.name,
    STRUCT(
        lat:=CAST(c.latitude AS double),
        lon:=CAST(c.longitude AS double)
    ) AS location
FROM cinema c
PARTITION BY c.id
EMIT CHANGES;

-- Create Cinema Table
CREATE table cinema_table (
    id VARCHAR PRIMARY KEY,
    name VARCHAR,
    location STRUCT<lat DOUBLE, lon DOUBLE>
) WITH (
    kafka_topic='cinema',
    value_format='avro',
    partitions=1
);

-- Create Shows Stream and Enrich it
CREATE STREAM shows_stream WITH (
    KAFKA_TOPIC = 'movie_db.shows',
    VALUE_FORMAT = 'AVRO'
);

CREATE STREAM enriched_shows WITH (
    kafka_topic='shows',
    value_format='avro'
) AS
SELECT
    s.id AS id,
    CAST(s.price AS int) AS price,
    STRUCT(
        title:=m.title,
        director:=m.director,
        actors:=m.actors,
        description:=m.description
    ) AS movie,
    STRUCT(
        cinema_name:=c.name,
        location:=c.location
    ) AS cinema
FROM shows_stream s
JOIN movie m ON s.movie_id = m.id
JOIN cinema_table c ON s.cinema_id = c.id
PARTITION BY s.id
EMIT CHANGES;

-- Register Elasticsearch Sink Connector
CREATE SOURCE CONNECTOR es_sink WITH (
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