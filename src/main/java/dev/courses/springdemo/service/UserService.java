package dev.courses.springdemo.service;

import dev.courses.springdemo.controller.error.NotFoundException;
import dev.courses.springdemo.repository.UserRepository;
import dev.courses.springdemo.repository.model.User;
import dev.courses.springdemo.service.dto.UserDto;
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

    public UserDto getUserById(long id) {
        return userRepository.findById(id)
                .map(UserDto::new)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id=%d", id)));
    }

    public UserDto createUser(UserDto userDto) {
        var savedUser = userRepository.save(new User(userDto));
        return new UserDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .toList();
    }

    public Page<UserDto> getAllUsersByPage(int pageNumber, int size) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(pageNumber, size));

        List<UserDto> userDtoList = userPage.getContent().stream()
                .map(UserDto::new)
                .toList();

        return new PageImpl<>(userDtoList, userPage.getPageable(), userPage.getTotalPages());
    }

    public UserDto updateUserById(long id, UserDto userDto) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("User not found with id=%d", id)));
        var userToUpdate = new User(userDto);
        userToUpdate.setId(id);
        var updatedUser = userRepository.save(userToUpdate);
        return new UserDto(updatedUser);
    }

    public void deleteUserById(long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("User not found with id=%d", id)));
        userRepository.deleteById(id);
    }
}
