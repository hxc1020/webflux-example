import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class CustomerHandler {

    private final CustomerRepository repository;

    public CustomerHandler(CustomerRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> getPerson(ServerRequest request) {
        int personId = Integer.valueOf(request.pathVariable("id"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Customer> personMono = this.repository.getById(personId);
        return personMono
                .flatMap(person -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(person)))
                .switchIfEmpty(notFound);
    }


    public Mono<ServerResponse> createPerson(ServerRequest request) {
        Mono<Customer> person = request.bodyToMono(Customer.class);
        return ServerResponse.ok().build(this.repository.save(person));
    }

    public Mono<ServerResponse> listPeople(ServerRequest request) {
        Flux<Customer> people = this.repository.all();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(people, Customer.class);
    }

}
