package com.suggestion.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;

public class SuggestionServiceTest {
    private SuggestionService suggestionService;

    @BeforeEach
    void setup() {
        suggestionService = new SuggestionService();
        ReflectionTestUtils.setField(suggestionService, "dictionary", Set.of("apple", "apply", "angle", "ample"));
    }

    @Test
    void suggestWhenWordIsCorrect() {
        List<String> suggestions = suggestionService.suggest("apple");
        Assertions.assertTrue(suggestions.isEmpty(), "Should return empty list when word is valid");
    }

    @Test
    void suggestWhenWordIsIncorrect() {
        List<String> suggestions = suggestionService.suggest("appl");
        Assertions.assertFalse(suggestions.isEmpty(), "Should return suggestions");
        Assertions.assertTrue(suggestions.contains("apple") || suggestions.contains("apply"));
    }

    @Test
    void suggestWhenWordIsEmpty() {
        List<String> suggestions = suggestionService.suggest("");
        Assertions.assertTrue(suggestions.isEmpty(), "Should return empty list on empty word");
    }
}
