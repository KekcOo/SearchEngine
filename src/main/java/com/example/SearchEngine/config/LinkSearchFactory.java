package com.example.SearchEngine.config;

import com.example.SearchEngine.entity.Site;
import com.example.SearchEngine.init.LinkSearch;
import com.example.SearchEngine.services.PageService;
import com.example.SearchEngine.services.SiteService;
import com.example.SearchEngine.utils.ConnectionResponse;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.concurrent.ExecutorService;


@Slf4j
@Component
@ToString
public class LinkSearchFactory {
    private final SiteService siteService;
    private final PageService pageService;
    private final ConnectionResponse connectionResponse;
    private final ExecutorService dbExecutor;


    public LinkSearchFactory(SiteService siteService, PageService pageService, ConnectionResponse connectionResponse, ExecutorService dbExecutor) {
        this.siteService = siteService;
        this.pageService = pageService;
        this.connectionResponse = connectionResponse;
        this.dbExecutor = dbExecutor;
    }
    public LinkSearch createLinkSearch(String url, Site site) {
        log.info("Create Link Search");
        log.info(connectionResponse.toString());
        return new LinkSearch(siteService, pageService, url, site,connectionResponse, dbExecutor);
    }

}
