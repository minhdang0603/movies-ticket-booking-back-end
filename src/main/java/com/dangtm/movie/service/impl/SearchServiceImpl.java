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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchServiceImpl implements SearchService {

    ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<MovieIndex> autoSuggestMovie(String query) {
        List<String> searchFields = List.of("title", "director", "actors");

        Query suggestQuery = ElasticsearchUtil.createMovieAutoSuggestQuery(query, searchFields);

        SearchHits<MovieIndex> hits = elasticsearchOperations.search(suggestQuery, MovieIndex.class);

        return hits.stream().map(SearchHit::getContent).toList();
    }

    @Override
    public List<CinemaIndex> autoSuggestCinema(String query, double lat, double lon, String distance) {
        Query suggestQuery = ElasticsearchUtil.createCinemaAutoSuggestQuery(query, "name", lat, lon, distance);

        SearchHits<CinemaIndex> hits = elasticsearchOperations.search(suggestQuery, CinemaIndex.class);

        return hits.stream().map(SearchHit::getContent).toList();
    }
}
