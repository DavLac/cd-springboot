package dev.courses.springdemo.controller;

import dev.courses.springdemo.service.UserService;
import dev.courses.springdemo.service.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(value = "users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    @Operation(description = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserDto> getUserById(
            @PathVariable
            @Parameter(description = "Existing user ID")
            long userId
    ) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("all") // -> not restfull name. it's used because of pagination using the right path
    @Operation(description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping
    @Operation(description = "Get all users by page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<UserDto>> getAllUsersByPage(
            @RequestParam(defaultValue = "1", required = false)
            @Positive
            @Max(10000)
            @Parameter(description = "Page number")
            int pageNumber,
            @RequestParam(defaultValue = "50", required = false)
            @Positive
            @Max(500)
            @Parameter(description = "Number of elements by page")
            int pageSize) {
        return ResponseEntity.ok(userService.getAllUsersByPage(pageNumber - 1, pageSize));
    }

    @PostMapping
    @Operation(description = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserDto> createUser(
            @RequestBody
            @Valid
            @Parameter(description = "User details to create")
            UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    /**
     * Not restful endpoint. Made to test wiremock IT tests (capturing request body)
     */
    @PostMapping("people")
    @Operation(description = "Create a star wars people")
    public ResponseEntity<UserDto> createPeopleFakeEndpoint(
            @RequestBody
            UserDto userDto) {
        return ResponseEntity.ok(userService.createPeople(userDto));
    }

    /**
     * Not restful endpoint. Made to test wiremock IT tests (capturing parameters)
     */
    @GetMapping("people")
    @Operation(description = "Create a star wars people")
    public ResponseEntity<UserDto> getPeopleFakeEndpoint() {
        return ResponseEntity.ok(userService.getPeople());
    }

    @PutMapping("{userId}")
    @Operation(description = "Update a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserDto> updateUserById(
            @PathVariable
            @Parameter(description = "Existing user ID")
            long userId,
            @RequestBody
            @Valid
            @Parameter(description = "User details to modify")
            UserDto userDto
    ) {
        return ResponseEntity.ok(userService.updateUserById(userId, userDto));
    }

    @DeleteMapping("{userId}")
    @Operation(description = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUserById(
            @PathVariable
            @Parameter(description = "Existing user ID")
            long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("starwars")
    @Operation(description = "Create a user using Star Wars API by a character ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserDto> createStarWarsUser(
            @Min(1)
            @Max(83)
            @RequestParam
            @Parameter(description = "ID of a star wars character. [Min = 1 - Max = 83]", example = "1")
            long starWarsCharacterId
    ) {
        return ResponseEntity.ok(userService.createStarWarsUser(starWarsCharacterId));
    }

}
