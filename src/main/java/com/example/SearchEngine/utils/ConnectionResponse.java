package com.example.SearchEngine.utils;



import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Scope("prototype")
@Getter
@Setter
public class ConnectionResponse {
    @Value("${indexing-settings.responses.userAgent}")
    private  String userAgent;
    @Value("${indexing-settings.responses.referrer}")
    private  String referrer;
    @Value("${indexing-settings.responses.maxBodySize}")
    private  int maxBodySize;

public  Connection.Response getConnectionResponse(String url) throws IOException {

    try {
        return Jsoup.connect(url)
                .ignoreHttpErrors(true)
                .userAgent(userAgent)
                .referrer(referrer)
                .maxBodySize(maxBodySize)
                .timeout(10000)
                .execute()
                ;
    } catch (Exception e) {
       log.error("Ошибка с url: {} {}",url, e.getMessage());
       throw e;
    }
}

}
