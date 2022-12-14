package com.example.Spring_boot_demo.service;

import com.example.Spring_boot_demo.controller.error.NotFoundException;
import com.example.Spring_boot_demo.repository.model.User;
import com.example.Spring_boot_demo.repository.model.UserRepository;
import com.example.Spring_boot_demo.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserDTO getUserByID(long id) {
        return userRepository.findById(id)
                .map(UserDTO::new)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    //public List<UserDTO> getAllUser();
    public UserDTO createUser(UserDTO userDTO) {
        var savedUser = userRepository.save(new User(userDTO));
        return new UserDTO(savedUser);
    }

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .toList();
    }

    public UserDTO updateUserById(long id, UserDTO userDTO){
        var toUpdateUser = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));
        toUpdateUser.setName(userDTO.getName());
        toUpdateUser.setAge(userDTO.getAge());
        toUpdateUser.setHeightInCm(userDTO.getHeightInCm());
        userRepository.save(toUpdateUser);
        return new UserDTO(toUpdateUser);
    }

    public void deleteUserById(long id){
        var deletedUser = userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));
        userRepository.deleteById(id);
    }

}
