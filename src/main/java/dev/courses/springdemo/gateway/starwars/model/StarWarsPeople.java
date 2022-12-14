package dev.courses.springdemo.gateway.starwars.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StarWarsPeople {
    private Long id;
    private String name;
    private Integer height;
    @JsonProperty(value = "birth_year")
    private String birthYear;
}
