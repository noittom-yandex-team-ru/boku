package com.alexey.transactionsapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Banner {

    @Value("${server.port}")
    private int serverPort;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    @EventListener
    public void onStartup(ApplicationStartedEvent event) {

        log.info("""
                
                
                Dear user, please use swagger URL: http://localhost:{}{}
                
                
                """,
                serverPort, swaggerPath);
    }

}
