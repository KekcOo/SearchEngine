package com.example.SearchEngine.init;

import com.example.SearchEngine.entity.Page;
import com.example.SearchEngine.entity.Site;
import com.example.SearchEngine.services.PageService;
import com.example.SearchEngine.services.SiteService;
import com.example.SearchEngine.utils.ConnectionResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;

@Slf4j
public class LinkSearch extends RecursiveAction {

    private final SiteService siteService;
    private final PageService pageService;
    private final String url;
    @Getter
    private final Site site;
    private final ExecutorService dbExecutor;
    private final ConnectionResponse connectionResponse;

    private static final Map<String, Boolean> processedUrls = new ConcurrentHashMap<>();

    private static final Pattern URL_FILTER = Pattern.compile(
            "^https?://.*" +
                    "(?<!\\.(jpg|pdf|doc|jpeg|zip|image|png|gif|rtf|ppt|mpeg|avi" +
                    "|xml|xls|xlsx|txt|docx|pptx|php|bmp|rar|mp4|mp3|mkv|svg))$"
            , Pattern.CASE_INSENSITIVE);


    public LinkSearch(SiteService siteService, PageService pageService, String url, Site site, ConnectionResponse connectionResponse, ExecutorService dbExecutor) {
        this.siteService = siteService;
        this.pageService = pageService;
        this.url = url;
        this.site = site;
        this.connectionResponse = connectionResponse;
        this.dbExecutor = dbExecutor;
    }

    @Override
    protected void compute() {
        if (Thread.currentThread().isInterrupted()) {
            log.warn("Прерванная задача: {}", url);
            return;
        }
        if (processedUrls.putIfAbsent(url, true) != null) {
            return;
        }
        try {
            Connection.Response response = connectionResponse.getConnectionResponse(url);

            String contentType = response.contentType();
            if (contentType == null || !(contentType.startsWith("text/") || contentType.contains("application/xml"))) {
                return;
            }
            processPage(response);
            Elements links = response.parse().select("a[href]");
            invokeAll(collectTasks(links));

        } catch (org.jsoup.UnsupportedMimeTypeException e) {
            log.error("Пропущен неподдерживаемый URL: {} (тип MIME: {})", url, e.getMimeType());
        } catch (Exception e) {
            log.error(e.getMessage() ,e.getLocalizedMessage(),e.getClass());
        }
    }

    private List<LinkSearch> collectTasks(Elements links) {
        List<LinkSearch> tasks = new ArrayList<>();
        for (Element link : links) {
            if (isVaLid(link.attr("abs:href").toLowerCase(), site.getUrl())) {
                tasks.add(new LinkSearch(siteService, pageService,
                        link.attr("abs:href").toLowerCase().split("\\?")[0],
                        site, connectionResponse, dbExecutor));
            }
        }
        return tasks;
    }

    private void processPage(Connection.Response response) {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }
        try {
            Page page =  CreatorEntity.createPage(site,response,url);
            dbExecutor.submit(() -> pageService.save(page));
        } catch (Exception e) {
            log.error("Ошибка странице  {}: {}", url, e.getMessage());
        }
    }

    private static boolean isVaLid(String href, String startUrl) {
        try {
            URL current = new URL(href);
            URL base = new URL(startUrl);
            if (!base.getHost().equals(current.getHost())) {
                return false;
            }
            return  href.startsWith(startUrl) &&
                    !href.contains("?method") &&
                    !href.contains("go?") &&
                    !href.contains("sort") &&
                    !href.contains("files") &&
                    !href.contains("page") &&
                    !href.contains("#") &&
                    !href.contains("vkontakte") &&
                    URL_FILTER.matcher(href).matches();
        } catch (MalformedURLException e) {
            log.debug("Неверный URL: {}", href);
            return false;
        }

    }

}