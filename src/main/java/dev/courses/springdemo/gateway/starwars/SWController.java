package dev.courses.springdemo.gateway.starwars;

import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import dev.courses.springdemo.gateway.starwars.resttemplate.StarWarsGatewayRestTemplate;
import dev.courses.springdemo.gateway.starwars.webclient.StarWarsGatewayWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "sw")
@RequiredArgsConstructor
public class SWController {

    private final StarWarsGatewayWebClient starWarsGatewayWebClient;
    private final StarWarsGatewayRestTemplate starWarsGatewayRestTemplate;

    @GetMapping("webclient/{id}")
    public StarWarsPeople getPeopleByIdWebclient(@PathVariable long id) {
        var response = starWarsGatewayWebClient.getPeopleById(id);
        response.setId(id);
        return response;
    }

    @GetMapping("restTemplate/{id}")
    public StarWarsPeople getPeopleByIdRestTemplate(@PathVariable long id) {
        var response = starWarsGatewayRestTemplate.getPeopleById(id);
        response.setId(id);
        return response;
    }

}
