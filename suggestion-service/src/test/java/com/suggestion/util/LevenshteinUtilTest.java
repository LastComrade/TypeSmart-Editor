package com.suggestion.util;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

public class LevenshteinUtilTest {
    @Test
    void testSameStrings() {
        Assertions.assertEquals(0, LevenshteinUtil.calculate("apple", "apple"));
    }

    @Test
    void testSingleInsertion() {
        Assertions.assertEquals(1, LevenshteinUtil.calculate("aple", "apple"));
    }

    @Test
    void testSingleDeletion() {
        Assertions.assertEquals(1, LevenshteinUtil.calculate("apple", "appl"));
    }

    @Test
    void testCompletelyDifferent() {
        Assertions.assertEquals(5, LevenshteinUtil.calculate("apple", "fig"));
    }

    @Test
    void testEmptyStrings() {
        Assertions.assertEquals(0, LevenshteinUtil.calculate("", ""));
        Assertions.assertEquals(5, LevenshteinUtil.calculate("apple", ""));
    }
}
