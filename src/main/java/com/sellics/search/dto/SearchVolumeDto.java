package com.sellics.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SearchVolumeDto {
    private String keyword;
    private int score;
}
