package com.example.SearchEngine.config;

import com.example.SearchEngine.utils.ConnectionResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {
    @Bean
    public ExecutorService dbExecutor() {
        return Executors.newFixedThreadPool(5);
    }
    @Bean
    public ConnectionResponse connectionResponse() {
        return new ConnectionResponse();
    }
    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

}
