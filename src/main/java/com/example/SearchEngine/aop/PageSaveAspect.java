package com.example.SearchEngine.aop;

import com.example.SearchEngine.entity.Page;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PageSaveAspect {

    @AfterReturning(pointcut = "execution(* com.example.SearchEngine.services.PageService.save(..))", returning = "page")
    public void savePage(Page page) {

    }
}
