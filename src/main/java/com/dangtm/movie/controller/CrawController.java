package com.dangtm.movie.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.dangtm.movie.dto.response.ApiResponse;
import com.dangtm.movie.service.CrawlService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/crawl")
public class CrawController {

    private static final Logger log = LoggerFactory.getLogger(CrawController.class);
    CrawlService crawlService;

    @PostMapping()
    public ApiResponse<Void> crawl(
            @RequestParam(value = "type") String type, @RequestParam(value = "crawDate") Optional<Integer> crawDate) {

        log.info("Start crawling...");

        crawlService.crawl(type, crawDate);

        log.info("End crawling!");

        return ApiResponse.<Void>builder().message("Crawl finished").build();
    }
}
