import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class Routes {

    public RouterFunction<ServerResponse> rout() {
        CustomerRepository repository = new CustomerRepositoryImpl();
        CustomerHandler handler = new CustomerHandler(repository);

        return nest(
                path("/customer"),
                nest(accept(APPLICATION_JSON),
                        route(GET("/{id}"), handler::getCustomer)
                                .andRoute(method(HttpMethod.GET), handler::listCustomer)
                ).andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::saveCustomer)
        ).andNest(
                path("/product"),
                route(path("/"), serverRequest ->
                        ServerResponse.ok().contentType(APPLICATION_JSON)
                                .body(fromPublisher(Flux.just(new Product(1, "PC", 1000.00)), Product.class))
                )
        );
    }
}
