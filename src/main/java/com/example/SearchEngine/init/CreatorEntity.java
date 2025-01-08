package com.example.SearchEngine.init;

import com.example.SearchEngine.config.SiteConfig;
import com.example.SearchEngine.entity.Page;
import com.example.SearchEngine.entity.Site;
import com.example.SearchEngine.entity.Status;
import lombok.Data;
import org.jsoup.Connection;

import java.time.LocalDateTime;

@Data
public class CreatorEntity {

    public static Site createSite(SiteConfig config) {
        Site siteNew = new Site();
        String url = config.getUrl();
        siteNew.setUrl(url);
        siteNew.setName(config.getName());
        siteNew.setStatusTime(LocalDateTime.now());
        siteNew.setStatus(Status.INDEXING);
        return siteNew;
    }
    public static Page createPage(Site site, Connection.Response response,String url) {
        Page page = new Page();
        page.setSite(site);
        page.setPath(url.replace(site.getUrl(), ""));
        page.setCode(response.statusCode());
        page.setContent(response.body());
        return  page;
    }
}
