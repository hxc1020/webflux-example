package com.lin.webflux;

import com.lin.webflux.web.MyWebSocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

    @Bean
    public HandlerMapping webSocketMapping(UnicastProcessor<String> eventPublisher, Flux<String> events) {
        Map<String, Object> map = new HashMap<>();
        map.put("/chat", new MyWebSocketHandler(events, eventPublisher));
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setUrlMap(map);
        simpleUrlHandlerMapping.setOrder(10);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public UnicastProcessor<String> eventPublisher() {
        return UnicastProcessor.create();
    }

    @Bean
    public Flux<String> events(UnicastProcessor<String> eventPublisher) {
        return eventPublisher
                .replay(1) // 历史数据
                .autoConnect();
    }
}
