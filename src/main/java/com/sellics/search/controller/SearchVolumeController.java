package com.sellics.search.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sellics.search.dto.SearchVolumeDto;
import com.sellics.search.service.SearchVolumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchVolumeController {
    public static final String ESTIMATE_KEYWORD = "/estimate";

    private final SearchVolumeService searchVolumeService;

    @GetMapping(path = ESTIMATE_KEYWORD)
    @HystrixCommand(fallbackMethod = "hystrixFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public SearchVolumeDto estimateKeyword(@RequestParam("keyword") final String keyword) {
        int searchVolumeScore = searchVolumeService.calculateSearchVolume(keyword);
        return SearchVolumeDto.builder()
                              .keyword(keyword)
                              .score(searchVolumeScore)
                              .build();
    }

    public SearchVolumeDto hystrixFallback(final String keyword) {
        return SearchVolumeDto.builder().keyword(keyword).score(-1).build();    // SLA violation (10 seconds), erroneous result -1
    }

}
