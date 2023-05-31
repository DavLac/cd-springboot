package dev.courses.springdemo.gateway.starwars.mapper;

import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import dev.courses.springdemo.service.dto.UserDto;

import static dev.courses.springdemo.gateway.starwars.utils.SWDateUtils.getAgeFromDateOfBirth;

public class PeopleMapper {

    public static StarWarsPeople toPeople(UserDto dto) {
        return StarWarsPeople.builder()
                .id(dto.getId())
                .name(dto.getName())
                .birthYear(String.valueOf(dto.getAge()))
                .height(dto.getHeightInCm())
                .build();
    }

    public static UserDto toDto(StarWarsPeople people) {
        return UserDto.builder()
                .id(people.getId())
                .name(people.getName())
                .age(getAgeFromDateOfBirth(people.getBirthYear()))
                .heightInCm(people.getHeight())
                .build();
    }
}
