package dev.courses.springdemo.gateway.starwars;

import dev.courses.springdemo.controller.error.BadGatewayException;
import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StarWarsGateway {

    private static final String PEOPLE_PATH = "%s/people/{peopleId}";

    // private final RestTemplate restTemplate;

    @Value("${application.star-wars-api.url}")
    private String starWarsApiUrl;

    public StarWarsPeople getPeopleById(Long peopleId) {
        Map<String, ?> parameters = Map.of("peopleId", peopleId);
        try {
            return new RestTemplateBuilder().build().getForObject(
                    String.format(PEOPLE_PATH, starWarsApiUrl),
                    StarWarsPeople.class,
                    parameters
            );
        } catch (HttpClientErrorException ex) {
            throw new BadGatewayException("Error when calling Star Wars API", ex);
        }
    }

    public StarWarsPeople getPeopleByIdEasyWay(Long peopleId) {
        return new RestTemplateBuilder().build().getForObject(
                starWarsApiUrl + "/people/{peopleId}",
                StarWarsPeople.class,
                Map.of("peopleId", peopleId)
        );
    }
}
