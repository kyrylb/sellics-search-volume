package com.sellics.search.utils;

public class SearchScoreCalculator {

    /**
     * Returns an array with weight values for each partial search,
     * where the weight for the most partial search (one character) has the greatest value
     * So, basically weight("a") > weight("ab") > weight("abc").
     * Sum of the weights should be 100, for example: weight("a") + weight("ab") + weight("abc") = 100 for the "abcd" string
     *
     * Example: weights for partial searches for keyword "canon": weights=[40.0, 30.0, 20.0, 10.0],
     * where 40.0 is the weight for the search "c" and 10.0 is the weight for the search "cano", sum of them is 100.0
     */
    public static double[] getPartialSearchWeights(final String keyword) {
        if (keyword == null || keyword.length() < 2) {
            throw new IllegalArgumentException("Partial search is possible for keywords longer than 1 char !");
        }

        int maxPartialCharCount = keyword.length() - 1;
        double[] weights = new double[keyword.length() - 1];    // only for partial searches, so length-1

        int sumOfPartialCharCounts = maxPartialCharCount * (maxPartialCharCount + 1) / 2;    // sum of number sequence up to and including maxPartialCharNumber
        double weightCoefficient = 100.0 / sumOfPartialCharCounts;    // weight coefficient value per 1 position
        for (int i = 0; i < keyword.length() - 1; i++) {    // only for partial searches, so length-1
            double weight = (keyword.length() - (i + 1)) * weightCoefficient;   // 1-char search has the biggest weight, 2-char is less etc.
            weights[i] = weight;
        }

        return weights;
    }

    /**
     * Returns partial score for the single iteration(one partial search).
     *
     * The score depends on:
     * 1) Weight (the least full partial search has the biggest weight, i.e. weight("a") > weight("ab") > weight("abc"));
     * 2) Position in the autocomplete list - top position is minimum divider 1.
     *
     * Example: partial searches for keyword "canon" with weights=[40.0, 30.0, 20.0, 10.0], if partial search with "c" returned
     * full keyword "cannon" at the very beginning of the list:
     * weights[charCount-1] / autocompleteListPosition + 1 = 40.0 / 1 => 40.0
     *
     * If each partial search returns the top position, than the final score is: 40.0 / 1 + 30.0 / 1 + 20.0 / 1 + 10.0 / 1 => 100.0
     */
    public static double calculatePartialScore(final int autocompleteListPosition, final int charCount, final double[] partialSearchWeights) {
        if (autocompleteListPosition < 0) {
            throw new IllegalArgumentException("Position in the autocomplete list should be bigger than or equal 0 !");
        }
        if (partialSearchWeights == null || partialSearchWeights.length < charCount) {
            throw new IllegalArgumentException("Char count index is out of weight array bounds !");
        }

        return partialSearchWeights[charCount - 1] / (autocompleteListPosition * 0.1 + 1);  // top position returns the biggest score
    }

}
