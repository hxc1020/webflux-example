import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


public class CustomerRepositoryImpl implements CustomerRepository {

    private final Map<Integer, Customer> customers = new HashMap<>();

    public CustomerRepositoryImpl() {
        this.customers.put(1, new Customer("小明", 11));
        this.customers.put(2, new Customer("小红", 12));
    }

    @Override
    public Mono<Customer> getById(int id) {
        return Mono.justOrEmpty(this.customers.get(id));
    }

    @Override
    public Flux<Customer> all() {
        return Flux.fromIterable(this.customers.values());
    }

    @Override
    public Mono<Void> save(Mono<Customer> personMono) {
        return personMono.doOnNext(person -> {
            int id = customers.size() + 1;
            customers.put(id, person);
            System.out.format("Saved %s with id %d%n", person, id);
        }).thenEmpty(Mono.empty());
    }
}
