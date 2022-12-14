package dev.courses.springdemo.repository.model;

import dev.courses.springdemo.service.dto.UserDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private Integer heightInCm;

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.age = userDto.getAge();
        this.heightInCm = userDto.getHeightInCm();
    }
}
