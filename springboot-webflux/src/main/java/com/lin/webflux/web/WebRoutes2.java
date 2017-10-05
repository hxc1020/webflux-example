package com.lin.webflux.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class WebRoutes2 {

    @Bean
    public RouterFunction<ServerResponse> route2() {
        return RouterFunctions.route(
                GET("/user"), request -> {
                    Mono<User> user = Mono.just(new User("1", "a"));
                    return ok().body(fromPublisher(user, User.class));
                });
    }
}