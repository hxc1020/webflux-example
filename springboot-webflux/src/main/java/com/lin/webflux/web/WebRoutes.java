package com.lin.webflux.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class WebRoutes {

    private UserHandler userHandler;

    public WebRoutes(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> route() {

        return RouterFunctions.route(
                GET("/"), request -> {
                    return ServerResponse.permanentRedirect(URI.create("index.html")).build();
                }).andNest(
                accept(MediaType.TEXT_EVENT_STREAM), RouterFunctions.route(GET("/msg"), userHandler::sendMessage
                ).andRoute(GET("/msg/{id}/{name}"), userHandler::addMsg)
        );
    }
}
