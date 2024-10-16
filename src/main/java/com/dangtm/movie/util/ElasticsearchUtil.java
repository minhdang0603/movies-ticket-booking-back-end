package com.dangtm.movie.util;

import java.util.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;

public class ElasticsearchUtil {

    private static final String DISTANCE_UNIT = "km";
    private static final String GEO_DISTANCE_FIELD = "location";
    private static final String AUTOCOMPLETE = "autocomplete";

    public static Query createMovieAutoSuggestQuery(String query, List<String> searchFields) {
        return NativeQuery.builder()
                .withQuery(q ->
                        q.multiMatch(mm -> mm.fields(searchFields).query(query).type(TextQueryType.BoolPrefix)))
                .withPageable(PageRequest.of(0, 10))
                .build();
    }

    public static Query createCinemaAutoSuggestQuery(
            String query, String searchField, double lat, double lon, String distance) {
        return NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b.must(m ->
                        m.matchPhrasePrefix(mm -> mm.field(searchField).query(query)))))
                .withFilter(f -> f.geoDistance(g -> g.field(GEO_DISTANCE_FIELD)
                        .distance(distance + DISTANCE_UNIT)
                        .location(new GeoLocation.Builder()
                                .coords(List.of(lon, lat))
                                .build())))
                .withPageable(PageRequest.of(0, 10))
                .build();
    }
}
