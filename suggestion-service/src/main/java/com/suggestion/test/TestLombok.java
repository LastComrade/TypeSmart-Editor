package com.suggestion.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TestLombok {
    private final String name;

    public static void main(String[] args) {
        System.out.println(new TestLombok("Test").getName());
    }
}
