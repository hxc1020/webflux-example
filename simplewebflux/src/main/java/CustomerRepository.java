import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Mono<Customer> getById(int id);

    Flux<Customer> all();

    Mono<Void> save(Mono<Customer> customer);

}
