package dev.courses.springdemo.controller;

import dev.courses.springdemo.service.UserService;
import dev.courses.springdemo.service.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(value = "users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("{id}")
    @Operation(description = "Get user by ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // @GetMapping -> deactivated to let "get user by page" to be used
    @Operation(description = "Get all users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping
    @Operation(description = "Get all users by page")
    public ResponseEntity<Page<UserDto>> getAllUsersByPage(
            @RequestParam(defaultValue = "1", required = false)
            @Positive
            @Max(10000)
            int pageNumber,
            @RequestParam(defaultValue = "50", required = false)
            @Positive
            @Max(500)
            int size) {
        return ResponseEntity.ok(userService.getAllUsersByPage(pageNumber - 1, size));
    }

    @PostMapping
    @Operation(description = "Create a user")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PutMapping("{id}")
    @Operation(description = "Update a user by id")
    public ResponseEntity<UserDto> updateUserById(@PathVariable long id, @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.updateUserById(id, userDto));
    }

    @DeleteMapping("{id}")
    @Operation(description = "Delete user by id")
    public void deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
    }

}
