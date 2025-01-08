package com.example.SearchEngine.web.controllers;

import com.example.SearchEngine.dto.statistics.StatisticsResponse;
import com.example.SearchEngine.services.StatisticsService;
import com.example.SearchEngine.services.impl.StartIndexImpl;
import com.example.SearchEngine.web.response.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final StatisticsService statisticsService;
    private final StartIndexImpl indexingService;


    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
    @GetMapping("/startIndexing")
    public ResponseEntity<ResultResponse> startIndexing() {
        return ResponseEntity.ok(new ResultResponse("true", indexingService.startIndexing()));
    }
    @GetMapping("/stopIndexing")
    public ResponseEntity<String> stopIndexing() {
        String response = indexingService.stopIndexing();
        return ResponseEntity.ok(response);
    }

}
