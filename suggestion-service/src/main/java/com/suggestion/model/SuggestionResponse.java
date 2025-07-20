package com.suggestion.model;

import java.util.List;

public record SuggestionResponse(String word, List<String> suggestions) {
}
