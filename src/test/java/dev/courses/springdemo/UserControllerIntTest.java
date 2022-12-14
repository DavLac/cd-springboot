package dev.courses.springdemo;

import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import dev.courses.springdemo.gateway.starwars.webclient.StarWarsGatewayWebClient;
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
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private StarWarsGatewayWebClient starWarsGatewayWebClient;

    @Autowired
    private JsonUtils jsonUtils;

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_withGoodData_shouldReturnSavedUser() throws Exception {
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

        UserDto response = (UserDto) jsonUtils
                .deserializeResult(resultActions, UserDto.class);

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
    void getById_withExistingUser_shouldGetUser() throws Exception {
        User savedUser = userRepository.save(User.builder()
                .age(USER_AGE)
                .name(USER_NAME)
                .heightInCm(USER_HEIGHT)
                .build());

        ResultActions resultActions = mockMvc.perform(
                        get(USERS_PATH + "/" + savedUser.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        UserDto response = (UserDto) jsonUtils
                .deserializeResult(resultActions, UserDto.class);

        assertEquals(savedUser.getId(), response.getId());
        assertEquals(USER_NAME, response.getName());
        assertEquals(USER_AGE, response.getAge());
        assertEquals(USER_HEIGHT, response.getHeightInCm());
    }

    @Test
    void getByIdStarWars_withExistingPeople_shouldGetPeople() throws Exception {
        Mockito.when(starWarsGatewayWebClient.getPeopleById(1L))
                .thenReturn(StarWarsPeople.builder()
                        .name("luke")
                        .birthYear("19BBY")
                        .height(102)
                        .build());

        ResultActions resultActions = mockMvc.perform(
                        get("/sw/webclient/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        StarWarsPeople response = (StarWarsPeople) jsonUtils
                .deserializeResult(resultActions, StarWarsPeople.class);

        assertEquals("luke", response.getName());
        assertEquals("19BBY", response.getBirthYear());
        assertEquals(102, response.getHeight());
    }

}
