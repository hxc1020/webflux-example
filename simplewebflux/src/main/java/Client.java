import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

public class Client {

    private ExchangeFunction exchange = ExchangeFunctions.create(new ReactorClientHttpConnector());


    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.createPerson();
        client.printAllPeople();
    }

    public void printAllPeople() {
        URI uri = URI.create(String.format("http://%s:%d/customer", Server.HOST, Server.PORT));
        ClientRequest request = ClientRequest.method(HttpMethod.GET, uri).build();

        Flux<Customer> customers = exchange.exchange(request)
                .flatMapMany(response -> response.bodyToFlux(Customer.class));

        Mono<List<Customer>> peopleList = customers.collectList();
        System.out.println(peopleList.block());
    }

    public void createPerson() {
        URI uri = URI.create(String.format("http://%s:%d/customer", Server.HOST, Server.PORT));
        Customer jack = new Customer("大明", 21);

        ClientRequest request = ClientRequest.method(HttpMethod.POST, uri)
                .body(BodyInserters.fromObject(jack)).build();

        Mono<ClientResponse> response = exchange.exchange(request);

        System.out.println(response.block().statusCode());
    }

}
