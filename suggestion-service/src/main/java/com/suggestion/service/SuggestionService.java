package com.suggestion.service;

import com.suggestion.util.LevenshteinUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Slf4j
public class SuggestionService {
    private Set<String> dictionary;

    @PostConstruct
    public void init() throws IOException {
        log.info("Loading dictionary from file...");
        dictionary = new HashSet<>();

        ClassPathResource resource = new ClassPathResource("dictionary.txt");

        if (!resource.exists()) {
            log.error("Dictionary file not found: {}", resource.getPath());
            throw new IOException("Dictionary file not found");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String word;

            while ((word = reader.readLine()) != null) {
                dictionary.add(word.trim().toLowerCase());
            }
        }

        log.info("Dictionary loaded with {} words", dictionary.size());
    }

    public List<String> suggest(String word) {
        log.debug("Suggesting words for: {}", word);

        String finalWord = word.toLowerCase();
        List<String> suggestedWords = new ArrayList<>();

        if (finalWord.isEmpty()) {
            log.warn("Received empty word for suggestions");
            return Collections.emptyList();
        }

        if (dictionary.contains(finalWord)) {
            log.debug("Valid word as found in the dictionary: {}", finalWord);
            return suggestedWords;
        }

        if (dictionary.isEmpty()) {
            log.warn("Dictionary is empty, no suggestions available");
            return Collections.emptyList();
        }

        log.debug("Calculating suggestions for word: {}", finalWord);

        suggestedWords = dictionary.stream()
                .map(dictWord -> Map.entry(dictWord, LevenshteinUtil.calculate(finalWord, dictWord)))
                .sorted(Map.Entry.comparingByValue())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        log.debug("Suggestions for '{}': {}", finalWord, suggestedWords);

        return suggestedWords;
    }
}
