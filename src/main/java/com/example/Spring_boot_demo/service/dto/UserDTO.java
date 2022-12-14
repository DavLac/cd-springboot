package com.example.Spring_boot_demo.service.dto;

import com.example.Spring_boot_demo.repository.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    private int age;
    private int heightInCm;

    public UserDTO(User user) {

        this.id = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
        this.heightInCm = user.getHeightInCm();
    }
}
