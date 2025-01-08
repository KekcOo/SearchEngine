package com.example.SearchEngine.services.impl;

import com.example.SearchEngine.entity.Site;
import com.example.SearchEngine.exception.EntityNotFoundException;
import com.example.SearchEngine.repository.SiteRepository;
import com.example.SearchEngine.services.SiteService;
import com.example.SearchEngine.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    @Override
    public List<Site> findAll() {
        return siteRepository.findAll();
    }

    @Override
    public Site findById(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(MessageFormat.format("site с id {0} не найден!",id)));
    }

    @Override
    public Site save(Site site) {
        return siteRepository.save(site);
    }

    @Override
    public Site update(Site site) {
        Site savedSite = findById(site.getId());
        BeanUtils.copyNonNullProperties(site, savedSite);
        return siteRepository.save(savedSite);
    }

    @Override
    public void delete(Long id) {
    siteRepository.deleteById(id);
    }

    @Override
    public Site findByName(String name) {
        return siteRepository.findByName(name);
    }
}
