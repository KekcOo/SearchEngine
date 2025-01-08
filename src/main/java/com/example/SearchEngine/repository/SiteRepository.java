package com.example.SearchEngine.repository;

import com.example.SearchEngine.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    public Site findByName(String name);
}
