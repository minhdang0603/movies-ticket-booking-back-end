package com.dangtm.movie.service;

import java.util.*;

import org.openqa.selenium.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.dangtm.movie.entity.*;
import com.dangtm.movie.repository.*;

public interface CrawlService {
    @PreAuthorize("hasRole('ADMIN')")
    void crawl(String type, Optional<Integer> crawDate);
}
