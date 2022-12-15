package dev.courses.springdemo;

import dev.courses.springdemo.gateway.starwars.StarWarsGateway;
import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import dev.courses.springdemo.repository.UserRepository;
import dev.courses.springdemo.repository.model.User;
import dev.courses.springdemo.service.dto.UserDto;
import dev.courses.springdemo.utils.JsonUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static dev.courses.springdemo.utils.JsonUtils.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringdemoApplication.class)
@AutoConfigureMockMvc
@EnableWebMvc
@ComponentScan("dev.courses.springdemo")
class UserControllerIntTest {

    public static final String USER_NAME = "john";
    public static final int USER_AGE = 18;
    public static final int USER_HEIGHT = 180;
    public static final String USERS_PATH = "/users";
    public static final String STAR_WARS_USER_PATH = "/users/starwars";
    public static final long PEOPLE_ID = 1L;
    public static final String SW_NAME = "luke";
    public static final String SW_BIRTH_YEAR = "18BBY";
    public static final int SW_AGE = 68;
    public static final int SW_HEIGHT = 102;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private StarWarsGateway starWarsGateway;

    @Autowired
    private JsonUtils jsonUtils;

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_withGoodData_shouldReturnSavedUser() throws Exception {
        // init test
        int usersBefore = userRepository.findAll().size();

        var request = UserDto.builder()
                .age(USER_AGE)
                .name(USER_NAME)
                .heightInCm(USER_HEIGHT)
                .build();

        // make request
        ResultActions resultActions = mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk());

        // read response
        UserDto response = jsonUtils.deserializeResult(resultActions, UserDto.class);

        // assertions
        assertNotNull(response.getId());
        assertEquals(USER_NAME, response.getName());
        assertEquals(USER_AGE, response.getAge());
        assertEquals(USER_HEIGHT, response.getHeightInCm());

        // database assertions
        int usersAfter = userRepository.findAll().size();
        assertEquals(usersBefore + 1, usersAfter);
        assertEquals(1, usersAfter);
        assertNotNull(userRepository.findById(response.getId()).get());
    }

    @Test
    void getUserById_withExistingUser_shouldGetUser() throws Exception {
        // init test
        User savedUser = userRepository.save(User.builder()
                .age(USER_AGE)
                .name(USER_NAME)
                .heightInCm(USER_HEIGHT)
                .build());

        // make request
        ResultActions resultActions = mockMvc.perform(
                        get(USERS_PATH + "/" + savedUser.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // read response
        UserDto response = jsonUtils.deserializeResult(resultActions, UserDto.class);

        // assertions
        assertEquals(savedUser.getId(), response.getId());
        assertEquals(USER_NAME, response.getName());
        assertEquals(USER_AGE, response.getAge());
        assertEquals(USER_HEIGHT, response.getHeightInCm());
    }

    @Test
    void createStarWarsUser_withExistingSWCharacter_shouldCreateSWUser() throws Exception {
        // init test
        Mockito.when(starWarsGateway.getPeopleById(PEOPLE_ID))
                .thenReturn(StarWarsPeople.builder()
                        .name(SW_NAME)
                        .birthYear(SW_BIRTH_YEAR)
                        .height(SW_HEIGHT)
                        .build());

        int usersBefore = userRepository.findAll().size();

        // make request
        ResultActions resultActions = mockMvc.perform(
                        post(STAR_WARS_USER_PATH)
                                .param("starWarsCharacterId", String.valueOf(PEOPLE_ID))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // read response
        UserDto response = jsonUtils.deserializeResult(resultActions, UserDto.class);

        // assertions
        assertEquals(SW_NAME, response.getName());
        assertEquals(SW_AGE, response.getAge());
        assertEquals(SW_HEIGHT, response.getHeightInCm());

        // database assertions
        int usersAfter = userRepository.findAll().size();
        assertEquals(usersBefore + 1, usersAfter);
        assertEquals(1, usersAfter);
        assertNotNull(userRepository.findById(response.getId()).get());
    }

}
