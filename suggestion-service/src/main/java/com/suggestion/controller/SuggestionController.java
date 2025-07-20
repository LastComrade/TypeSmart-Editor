package com.suggestion.controller;

import com.suggestion.model.SuggestionResponse;
import com.suggestion.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@Slf4j
@RequiredArgsConstructor
public class SuggestionController {
    private final SuggestionService suggestionService;

    @GetMapping
    public ResponseEntity<SuggestionResponse> getSuggestions(@RequestParam String word) {
        log.debug("Received request for suggestions for word: {}", word);
        List<String> suggestions = suggestionService.suggest(word);

        if (suggestions.isEmpty()) {
            log.debug("No suggestions found for word: {}", word);
            return ResponseEntity.noContent().build();
        }

        log.debug("Found {} suggestions for word: {}", suggestions.size(), word);

        return ResponseEntity.ok(new SuggestionResponse(word, suggestions));
    }
}
