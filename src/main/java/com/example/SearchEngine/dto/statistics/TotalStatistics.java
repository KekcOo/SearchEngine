package com.example.SearchEngine.dto.statistics;

import lombok.Data;

@Data
public class TotalStatistics {
    private int sites;
    private int pages;
    private int lemmas;
    private boolean indexing;
}
