package com.lin.webflux.web;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Service
public class UserHandler {
    private Flux<User> users = Flux.just(new User("2", "lin"), new User("3", "li"));

    private HttpStatus status = HttpStatus.OK;

    Mono<ServerResponse> sendMessage(ServerRequest serverRequest) {
//        Flux<User> userFlux = Flux.zip(Flux.interval(Duration.ofMillis(2000)), users).map(Tuple2::getT2);
        return status(status).contentType(MediaType.TEXT_EVENT_STREAM)
//                .header("retry","1000000")
                .body(users, new ParameterizedTypeReference<User>() {
                }).doAfterSuccessOrError((serverResponse, throwable) -> {
                    users = Flux.empty();
                });
    }

    Mono<ServerResponse> addMsg(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        String name = serverRequest.pathVariable("name");
        users = Flux.just(new User(id, name));
        return ok().build();
    }
}
