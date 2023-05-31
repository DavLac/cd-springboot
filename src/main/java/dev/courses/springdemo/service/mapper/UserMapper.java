package dev.courses.springdemo.service.mapper;

import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import dev.courses.springdemo.repository.model.User;
import dev.courses.springdemo.service.dto.UserDto;

import static dev.courses.springdemo.gateway.starwars.utils.SWDateUtils.getAgeFromDateOfBirth;

public class UserMapper {
    public static UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .age(entity.getAge())
                .heightInCm(entity.getHeightInCm())
                .build();
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .heightInCm(dto.getHeightInCm())
                .build();
    }

    public static User toEntity(StarWarsPeople people) {
        return User.builder()
                .id(people.getId())
                .name(people.getName())
                .age(getAgeFromDateOfBirth(people.getBirthYear()))
                .heightInCm(people.getHeight())
                .build();
    }

    public static StarWarsPeople toStarWarsPeople(UserDto dto) {
        return StarWarsPeople.builder()
                .id(dto.getId())
                .name(dto.getName())
                .birthYear(String.valueOf(dto.getAge()))
                .height(dto.getHeightInCm())
                .build();
    }

    public static User toDto(StarWarsPeople people) {
        return User.builder()
                .id(people.getId())
                .name(people.getName())
                .age(getAgeFromDateOfBirth(people.getBirthYear()))
                .heightInCm(people.getHeight())
                .build();
    }
}
