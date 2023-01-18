package dev.courses.springdemo.service;

import dev.courses.springdemo.controller.error.NotFoundException;
import dev.courses.springdemo.gateway.starwars.StarWarsGateway;
import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import dev.courses.springdemo.repository.UserRepository;
import dev.courses.springdemo.repository.model.User;
import dev.courses.springdemo.service.dto.UserDto;
import dev.courses.springdemo.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StarWarsGateway starWarsGateway;

    public UserDto getUserById(long userId) {
        return userRepository.findById(userId)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id=%d", userId)));
    }

    public UserDto createUser(UserDto userDto) {
        var savedUser = userRepository.save(UserMapper.toEntity(userDto));
        return UserMapper.toDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public Page<UserDto> getAllUsersByPage(int pageNumber, int pageSize) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(pageNumber, pageSize));

        List<UserDto> userDtoList = userPage.getContent().stream()
                .map(UserMapper::toDto)
                .toList();

        return new PageImpl<>(userDtoList, userPage.getPageable(), userPage.getTotalPages());
    }

    public UserDto updateUserById(long userId, UserDto userDto) {
        getUserById(userId);
        var userToUpdate = UserMapper.toEntity(userDto);
        userToUpdate.setId(userId);
        var updatedUser = userRepository.save(userToUpdate);
        return UserMapper.toDto(updatedUser);
    }

    public void deleteUserById(long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    public UserDto createStarWarsUser(long starWarsCharacterId) {
        StarWarsPeople starWarsPeople = starWarsGateway.getPeopleById(starWarsCharacterId);
        var savedUser = userRepository.save(UserMapper.toEntity(starWarsPeople));
        return UserMapper.toDto(savedUser);
    }
}
