package com.example.SearchEngine.services;


import com.example.SearchEngine.entity.Site;

import java.util.List;

public interface SiteService {

    List<Site> findAll();

    Site findById(Long id);

    Site save(Site site);

    Site update(Site site);

    void delete(Long id);

    public Site findByName(String name);
}
