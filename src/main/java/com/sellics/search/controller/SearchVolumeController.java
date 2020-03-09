package com.sellics.search.controller;

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
    public SearchVolumeDto estimateKeyword(@RequestParam("keyword") final String keyword) {
        int searchVolumeScore = searchVolumeService.calculateSearchVolume(keyword);
        return SearchVolumeDto.builder().keyword(keyword).score(searchVolumeScore).build();
    }
}
