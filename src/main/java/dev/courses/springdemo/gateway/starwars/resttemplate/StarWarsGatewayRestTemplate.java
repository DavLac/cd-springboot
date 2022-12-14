package dev.courses.springdemo.gateway.starwars.resttemplate;

import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StarWarsGatewayRestTemplate {

    private final RestTemplate restTemplate;

    @Value("${application.star-wars-api.url}")
    private String starWarsApiUrl;

    public StarWarsPeople getPeopleById(Long peopleId) {
        return restTemplate.getForObject(
                String.format("%s/people/{peopleId}", starWarsApiUrl),
                StarWarsPeople.class,
                Map.of("peopleId", peopleId)
        );
    }
}
