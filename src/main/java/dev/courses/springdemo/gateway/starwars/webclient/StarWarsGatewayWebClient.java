package dev.courses.springdemo.gateway.starwars.webclient;

import dev.courses.springdemo.controller.error.BadGatewayException;
import dev.courses.springdemo.controller.error.NotFoundException;
import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class StarWarsGatewayWebClient {

    private final WebClient webClient;

    @Value("${application.star-wars-api.url}")
    private String starWarsApiUrl;

    public StarWarsPeople getPeopleById(Long peopleId) {
        return webClient.get()
                .uri(String.format("%s/people/{peopleId}", starWarsApiUrl), peopleId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus == HttpStatus.NOT_FOUND, response -> {
                    throw new NotFoundException(String.format("People not found with ID ='%d'", peopleId));
                })
                .onStatus(HttpStatus::isError, response -> {
                    throw new BadGatewayException(
                            String.format("Request failed when attempting to get Star Wars people with ID ='%d'.", peopleId));
                })
                .bodyToMono(StarWarsPeople.class)
                .block();
    }

}
