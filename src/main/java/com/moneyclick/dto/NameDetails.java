package com.moneyclick.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class NameDetails {
    private String docType;
    private String name;
    private List<String> possibleNames;
    private Set<Integer> matchedIndices;
    private int score;

    public void incrementScore(){
        score++;
    };
}
