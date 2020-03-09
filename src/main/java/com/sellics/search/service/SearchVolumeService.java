package com.sellics.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellics.search.utils.SearchScoreCalculator;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchVolumeService {
    private static final String AUTOCOMPLETE_BASE_URL = "https://completion.amazon.com";
    private static final String AUTOCOMPLETE_URI = "/search/complete?search-alias=aps&mkt=1&q={keyword}";

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public SearchVolumeService(final RestTemplateBuilder builder, final ObjectMapper mapper) {
        restTemplate = builder.rootUri(AUTOCOMPLETE_BASE_URL).build();
        this.mapper = mapper;
    }

    /**
     * Calculates search volume by running partial searches and summing up scores for each of them
     * Each partial score depends on the weight (which, in its turn, depends on char count for each search) and
     * position in the autocomplete list.
     *
     */
    public int calculateSearchVolume(final String keyword) {
        double[] partialSearchWeights = SearchScoreCalculator.getPartialSearchWeights(keyword);

        double searchVolume = 0.0;
        for (int i = keyword.length() -1; i > 0; i--) {
            String partialKeyword = keyword.substring(0, i);
            List<String> autocompleteList = getAutocompleteList(partialKeyword);
            int listPosition = autocompleteList.indexOf(keyword);
            if (listPosition < 0) {
                break;
            }
            searchVolume += SearchScoreCalculator.calculatePartialScore(listPosition, partialKeyword.length(), partialSearchWeights);
        }
        return (int)searchVolume;
    }

    private List<String> getAutocompleteList(final String partialKeyword) {
        if (partialKeyword == null || partialKeyword.length() < 1) {
            throw new IllegalArgumentException("Partial keyword length should be bigger than 1 char !");
        }
        String jsonArray = restTemplate.getForObject(AUTOCOMPLETE_URI, String.class, partialKeyword);

        List<String> autocompleteList = Collections.emptyList();
        try {
            autocompleteList = (List<String>) mapper.readValue(jsonArray, Object[].class)[1];   // autocomplete list is at index 1
        } catch (IOException e) {
            e.printStackTrace();
        }
        return autocompleteList;
    }

}
