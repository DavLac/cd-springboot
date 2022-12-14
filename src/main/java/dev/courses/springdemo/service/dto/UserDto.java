package dev.courses.springdemo.service.dto;

import dev.courses.springdemo.repository.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotEmpty
    @Size(min = 3, max = 30)
    private String name;

    @NotNull
    @Positive
    @Max(150)
    private Integer age;

    @NotNull
    @Min(50)
    @Max(300)
    private Integer heightInCm;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
        this.heightInCm = user.getHeightInCm();
    }
}
