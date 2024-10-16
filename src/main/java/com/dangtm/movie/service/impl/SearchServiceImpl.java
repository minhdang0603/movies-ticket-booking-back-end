package com.dangtm.movie.service.impl;

import java.util.List;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import com.dangtm.movie.entity.index.CinemaIndex;
import com.dangtm.movie.entity.index.MovieIndex;
import com.dangtm.movie.service.SearchService;
import com.dangtm.movie.util.ElasticsearchUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchServiceImpl implements SearchService {

    ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<MovieIndex> autoSuggestMovie(String query) {
        List<String> searchFields = List.of(
                "title",
                "title._2gram",
                "title._3gram",
                "director",
                "director._2gram",
                "director._3gram",
                "actors._2gram",
                "actors._3gram");

        Query suggestQuery = ElasticsearchUtil.createMovieAutoSuggestQuery(query, searchFields);

        SearchHits<MovieIndex> hits = elasticsearchOperations.search(suggestQuery, MovieIndex.class);

        return hits.stream().map(SearchHit::getContent).toList();
    }

    @Override
    public List<CinemaIndex> autoSuggestCinema(String query, double lat, double lon, String distance) {
        String searchField = "name";

        Query suggestQuery = ElasticsearchUtil.createCinemaAutoSuggestQuery(query, searchField, lat, lon, distance);

        SearchHits<CinemaIndex> hits = elasticsearchOperations.search(suggestQuery, CinemaIndex.class);
        return hits.stream().map(SearchHit::getContent).toList();
    }
}
