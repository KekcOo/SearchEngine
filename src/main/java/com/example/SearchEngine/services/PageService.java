package com.example.SearchEngine.services;

import com.example.SearchEngine.entity.Page;

import java.util.List;

public interface PageService {

    List<Page> findAll();

    Page findById(Long id);

    Page save(Page page);

    Page update(Page page);

    void delete(Long id);

    public void saveAll(List<Page> pages);
}
