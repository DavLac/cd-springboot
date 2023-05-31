package dev.courses.springdemo.gateway.starwars;

import dev.courses.springdemo.controller.error.BadGatewayException;
import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StarWarsGateway {

    // private final RestTemplate restTemplate;

    @Value("${application.star-wars-api.url}")
    private String starWarsApiUrl;

    public StarWarsPeople getPeopleById(Long peopleId) {
        Map<String, ?> parameters = Map.of("peopleId", peopleId);
        try {
            return new RestTemplateBuilder().build().getForObject(
                    "%s/people/{peopleId}".formatted(starWarsApiUrl),
                    StarWarsPeople.class,
                    parameters
            );
        } catch (HttpClientErrorException ex) {
            throw new BadGatewayException("Error when calling Star Wars API", ex);
        }
    }

    /**
     * Fake non-existing API. Made to test wiremock IT tests with body matcher (capturing request body)
     */
    public StarWarsPeople fakeCallWithBody(StarWarsPeople request) {
        try {
            return new RestTemplateBuilder().build().postForObject(
                    "%s/people".formatted(starWarsApiUrl),
                    request,
                    StarWarsPeople.class
            );
        } catch (HttpClientErrorException ex) {
            throw new BadGatewayException("Error when calling Star Wars API", ex);
        }
    }

    /**
     * Fake non-existing API. Made to test wiremock IT tests with parameters matcher (capturing parameters)
     */
    public StarWarsPeople fakeCallWithParameters() {
        try {
            return new RestTemplateBuilder().build().getForObject(
                    "%s/people?param1=value1&param2=value2".formatted(starWarsApiUrl),
                    StarWarsPeople.class
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
