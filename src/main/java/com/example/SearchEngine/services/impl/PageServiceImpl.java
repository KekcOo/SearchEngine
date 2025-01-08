package com.example.SearchEngine.services.impl;

import com.example.SearchEngine.entity.Page;
import com.example.SearchEngine.exception.EntityNotFoundException;
import com.example.SearchEngine.repository.PageRepository;
import com.example.SearchEngine.services.PageService;
import com.example.SearchEngine.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    @Override
    public void saveAll(List<Page> pages) {
        pageRepository.saveAll(pages);
    }

    @Override
    public List<Page> findAll() {
        return pageRepository.findAll();
    }

    @Override
    public Page findById(Long id) {
        return pageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("site с id {0} не найден!", id)));
    }

    @Override
    public Page save(Page page) {
        return pageRepository.save(page);
    }

    @Override
    public Page update(Page page) {
        Page savedPage = findById(page.getId());
        BeanUtils.copyNonNullProperties(page, savedPage);
        return pageRepository.save(savedPage);
    }

    @Override
    public void delete(Long id) {
        pageRepository.deleteById(id);

    }
}
