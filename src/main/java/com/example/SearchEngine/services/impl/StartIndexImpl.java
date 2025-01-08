package com.example.SearchEngine.services.impl;

import com.example.SearchEngine.config.LinkSearchFactory;
import com.example.SearchEngine.config.SiteConfig;
import com.example.SearchEngine.config.SitesList;
import com.example.SearchEngine.entity.Site;
import com.example.SearchEngine.entity.Status;
import com.example.SearchEngine.init.CreatorEntity;
import com.example.SearchEngine.init.LinkSearch;

import com.example.SearchEngine.services.SiteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartIndexImpl {

    private final SiteService siteService;
    private final SitesList sitesList;
    private final LinkSearchFactory linkSearchFactory;
    private final ExecutorService dbExecutor;

    private Future<?> currentTask;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final  List<LinkSearch> tasks = new ArrayList<>();

    public synchronized String startIndexing() {
        if (isRunning.get())  return "Индексация уже запущена";
        isRunning.set(true);
        currentTask = executorService.submit(() -> {
            try {
                startIndex();
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                isRunning.set(false);
                shutdownPools();
            }
        });
        return "Indexing started successfully.";
    }

    public synchronized String stopIndexing() {
        if (!isRunning.get()) return "Индексация не запущена";

        updateSiteStatuses(tasks);

        if (currentTask != null)  currentTask.cancel(true);

        shutdownPools();
        isRunning.set(false);
        return "Индексация останавливается .";
    }

    public void startIndex() {
        log.info(linkSearchFactory.toString());
        log.info("start index");

        try {
            List<SiteConfig> siteConfig = sitesList.getSites();
            siteConfig.forEach(config -> {
                Site site = CreatorEntity.createSite(config);
                siteService.save(site);
                LinkSearch task = linkSearchFactory.createLinkSearch(config.getUrl(), site);
                tasks.add(task);
                pool.submit(task);
            });
            pool.shutdown();
            if (!pool.awaitTermination(30, TimeUnit.MINUTES)) {
                log.warn("Задачи индексации не были завершены в ожидаемое время. Принудительное завершение работы");
                pool.shutdownNow();
            }
            updateSiteStatuses(tasks);
        } catch (InterruptedException e) {
            log.error("Indexing interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            shutdownPools();
        }

    }


    public boolean isIndexingRunning() {
        return isRunning.get();
    }

    public void shutdownPools() {
        try {
            log.info("Завершение  ForkJoinPool...");
            pool.shutdown();
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Принудительное завершение ForkJoinPool...");
                pool.shutdownNow();
            }
            log.info("Завершение ExecutorService...");
            dbExecutor.shutdown();
            if (!dbExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                log.warn("Принудительное завершение ExecutorService...");
                dbExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Ошибка во время завершения пулов: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void updateSiteStatuses(List<LinkSearch> tasks) {
        tasks.forEach(task -> {
            Site site = siteService.findByName(task.getSite().getName());
            if (task.isCompletedNormally()) {
                site.setStatus(Status.INDEXED);
                site.setErrorText(null);
            } else if (task.isCancelled()) {
                site.setStatus(Status.FAILED);
                site.setErrorText("Индексация прервана пользователем.");
            } else {
                site.setStatus(Status.FAILED);
                site.setErrorText("Индексация завершилась с ошибкой.");
            }
            site.setStatusTime(LocalDateTime.now());
            siteService.save(site);
        });
    }
}