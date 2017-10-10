package com.lin.webflux.web;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.time.LocalDateTime;

public class MyWebSocketHandler implements WebSocketHandler {

    private Flux<String> message;
    private UnicastProcessor<String> eventPublisher;
    private LocalDateTime now;

    public MyWebSocketHandler(Flux<String> message, UnicastProcessor<String> eventPublisher) {
        this.message = message;
        this.eventPublisher = eventPublisher;
        this.now = LocalDateTime.now();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .subscribe(s -> eventPublisher.onNext(answerMsg(s)));

        return session.send(message.map(session::textMessage));
    }

    private String answerMsg(String question) {
        String time = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfYear() + " "
                + "  :  " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();
        switch (question) {
            case "你叫什么":
                return "robot";
            case "what's your name":
                return "robot";
            case "give me five":
                return "5";
            case "现在几点":
                return time;
            case "show me time":
                return time;
            case "ping":
                return "pong";
            case "where are you":
                return "http://localhost:9000";
            default:
                return "I don't know";
        }
    }
}
