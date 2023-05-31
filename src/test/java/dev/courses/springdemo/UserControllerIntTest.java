package dev.courses.springdemo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import dev.courses.springdemo.gateway.starwars.model.StarWarsPeople;
import dev.courses.springdemo.repository.UserRepository;
import dev.courses.springdemo.repository.model.User;
import dev.courses.springdemo.service.dto.UserDto;
import dev.courses.springdemo.utils.JsonUtils;
import dev.courses.springdemo.utils.WireMockServerFacade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.NestedServletException;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static dev.courses.springdemo.utils.JsonUtils.toJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
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
    public static final String USER_NAME_2 = "bob";
    public static final int USER_AGE_2 = 25;
    public static final int USER_HEIGHT_2 = 165;
    public static final String USERS_PATH = "/users";
    public static final String STAR_WARS_USER_PATH = "/users/starwars";
    public static final long PEOPLE_ID = 1L;
    public static final String SW_NAME = "Luke Skywalker";
    public static final String SW_BIRTH_YEAR = "19BBY";
    public static final int SW_AGE = 69;
    public static final int SW_HEIGHT = 172;
    public static final String GET_PEOPLE_SCENARIO = "Get people Scenario";
    public static final String FIRST_ATTEMPT = "First Attempt";
    public static final String SECOND_ATTEMPT = "Second Attempt";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final JsonUtils jsonUtils = new JsonUtils();

    private static final WireMockServerFacade wireMock = new WireMockServerFacade();

    @BeforeAll
    static void beforeAll() {
        wireMock.startServer();
    }

    @AfterAll
    static void afterAll() {
        wireMock.stopServer();
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
        wireMock.resetStubs();
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
        MvcResult result = mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // read response
        UserDto response = jsonUtils.toObject(result.getResponse().getContentAsString(), UserDto.class);

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
        MvcResult result = mockMvc.perform(
                        get(USERS_PATH + "/" + savedUser.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // read response
        UserDto response = jsonUtils.toObject(result.getResponse().getContentAsString(), UserDto.class);

        // assertions
        assertEquals(savedUser.getId(), response.getId());
        assertEquals(USER_NAME, response.getName());
        assertEquals(USER_AGE, response.getAge());
        assertEquals(USER_HEIGHT, response.getHeightInCm());
    }

    @Test
    void getUserById_withExistingUsers_shouldGetAllUsers() throws Exception {
        // init test
        User savedUser = userRepository.save(User.builder()
                .age(USER_AGE)
                .name(USER_NAME)
                .heightInCm(USER_HEIGHT)
                .build());

        User savedUser2 = userRepository.save(User.builder()
                .age(USER_AGE_2)
                .name(USER_NAME_2)
                .heightInCm(USER_HEIGHT_2)
                .build());

        // make request
        MvcResult result = mockMvc.perform(
                        get(USERS_PATH + "/all")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // read response
        List<UserDto> response = jsonUtils.toCollection(result.getResponse().getContentAsString(), new TypeReference<List<UserDto>>(){});

        // assertions
        assertEquals(savedUser.getId(), response.get(0).getId());
        assertEquals(USER_NAME, response.get(0).getName());
        assertEquals(USER_AGE, response.get(0).getAge());
        assertEquals(USER_HEIGHT, response.get(0).getHeightInCm());

        assertEquals(savedUser2.getId(), response.get(1).getId());
        assertEquals(USER_NAME_2, response.get(1).getName());
        assertEquals(USER_AGE_2, response.get(1).getAge());
        assertEquals(USER_HEIGHT_2, response.get(1).getHeightInCm());
    }

    @Test
    void createStarWarsUser_withExistingSWCharacter_shouldCreateSWUser() throws Exception {
        // init test
        wireMock.when(HttpMethod.GET, "/people/" + PEOPLE_ID)
                .thenRespondJsonString(HttpStatus.OK,
                        toJsonString(StarWarsPeople.builder()
                                .name(SW_NAME)
                                .birthYear(SW_BIRTH_YEAR)
                                .height(SW_HEIGHT)
                                .build()));

        int usersBefore = userRepository.findAll().size();

        // make request
        MvcResult result = mockMvc.perform(
                        post(STAR_WARS_USER_PATH)
                                .param("starWarsCharacterId", String.valueOf(PEOPLE_ID))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // read response
        UserDto response = jsonUtils.toObject(result.getResponse().getContentAsString(), UserDto.class);

        // assertions
        assertEquals(SW_NAME, response.getName());
        assertEquals(SW_AGE, response.getAge());
        assertEquals(SW_HEIGHT, response.getHeightInCm());

        wireMock.verify(HttpMethod.GET, "/people/" + PEOPLE_ID, 1);

        // database assertions
        int usersAfter = userRepository.findAll().size();
        assertEquals(usersBefore + 1, usersAfter);
        assertEquals(1, usersAfter);
        assertNotNull(userRepository.findById(response.getId()).get());
    }

    @Test
    void createStarWarsUser_withExistingSWCharacter_shouldCreateSWUser_usingWireMockFile() throws Exception {
        // init test
        wireMock.when(HttpMethod.GET, "/people/" + PEOPLE_ID)
                .thenRespondJsonFile(HttpStatus.OK, "swapi/people/valid-people-response.json");

        // make request
        MvcResult result = mockMvc.perform(
                        post(STAR_WARS_USER_PATH)
                                .param("starWarsCharacterId", String.valueOf(PEOPLE_ID))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // read response
        UserDto response = jsonUtils.toObject(result.getResponse().getContentAsString(), UserDto.class);

        // assertions
        assertEquals(SW_NAME, response.getName());
        assertEquals(SW_AGE, response.getAge());
        assertEquals(SW_HEIGHT, response.getHeightInCm());

        wireMock.verify(HttpMethod.GET, "/people/" + PEOPLE_ID, 1);
    }

    @Test
    void createStarWarsUser_withGatewayTimeout_shouldThrowAnError() throws Exception {
        // init test
        wireMock.when(HttpMethod.GET, "/people/" + PEOPLE_ID)
                .thenRespondWithoutBody(HttpStatus.GATEWAY_TIMEOUT);

        // make request
        Throwable exception = assertThrows(NestedServletException.class, () ->
                mockMvc.perform(post(STAR_WARS_USER_PATH)
                        .param("starWarsCharacterId", String.valueOf(PEOPLE_ID))
                        .contentType(MediaType.APPLICATION_JSON)));
        assertEquals("Request processing failed; nested exception is org.springframework.web.client.HttpServerErrorException$GatewayTimeout: 504 Gateway Timeout: [no body]",
                exception.getMessage());

        wireMock.verify(HttpMethod.GET, "/people/" + PEOPLE_ID, 1);
    }

    @Test
    void createStarWarsUser_caseCallSameMock3TimesWithDifferentResponses() throws Exception {
        // init wiremocks
        String peopleUrl = "/people/" + PEOPLE_ID;

        // For this specific case (calling same mock 3 times with different responses)
        // We need to create our stub ourselves, without the "when" facade method using scenarios

        // first attempt -> 504 gateway timeout
        wireMock.getServer()
                .stubFor(
                        WireMock.request(GET.name(), urlPathEqualTo(peopleUrl))
                                .inScenario(GET_PEOPLE_SCENARIO)
                                .whenScenarioStateIs(STARTED)
                                .willReturn(aResponse().withStatus(HttpStatus.GATEWAY_TIMEOUT.value()))
                                .willSetStateTo(FIRST_ATTEMPT)
                );

        // second attempt -> 200 ok
        wireMock.getServer()
                .stubFor(
                        WireMock.request(GET.name(), urlPathEqualTo(peopleUrl))
                                .inScenario(GET_PEOPLE_SCENARIO)
                                .whenScenarioStateIs(FIRST_ATTEMPT)
                                .willReturn(
                                        aResponse()
                                                .withStatus(HttpStatus.OK.value())
                                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                .withBodyFile("swapi/people/valid-people-response.json")
                                )
                                .willSetStateTo(SECOND_ATTEMPT)
                );

        // third attempt -> 200 ok, other people returned
        wireMock.getServer()
                .stubFor(
                        WireMock.request(GET.name(), urlPathEqualTo(peopleUrl))
                                .inScenario(GET_PEOPLE_SCENARIO)
                                .whenScenarioStateIs(SECOND_ATTEMPT)
                                .willReturn(
                                        aResponse()
                                                .withStatus(HttpStatus.OK.value())
                                                .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                                .withBodyFile("swapi/people/valid-people2-response.json")
                                )
                );

        // make request - first attempt -----------------------------------------------------------
        Throwable exception = assertThrows(NestedServletException.class, () ->
                mockMvc.perform(post(STAR_WARS_USER_PATH)
                        .param("starWarsCharacterId", String.valueOf(PEOPLE_ID))
                        .contentType(MediaType.APPLICATION_JSON)));
        assertEquals("Request processing failed; nested exception is org.springframework.web.client.HttpServerErrorException$GatewayTimeout: 504 Gateway Timeout: [no body]",
                exception.getMessage());

        // make request - second attempt -----------------------------------------------------------
        MvcResult result200 = mockMvc.perform(
                        post(STAR_WARS_USER_PATH)
                                .param("starWarsCharacterId", String.valueOf(PEOPLE_ID))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // read response
        UserDto response200 = jsonUtils.toObject(result200.getResponse().getContentAsString(), UserDto.class);

        // assertions
        assertEquals(SW_NAME, response200.getName());
        assertEquals(SW_AGE, response200.getAge());
        assertEquals(SW_HEIGHT, response200.getHeightInCm());

        // make request - third attempt -----------------------------------------------------------
        MvcResult result200_2 = mockMvc.perform(
                        post(STAR_WARS_USER_PATH)
                                .param("starWarsCharacterId", String.valueOf(PEOPLE_ID))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // read response
        UserDto response200_2 = jsonUtils.toObject(result200_2.getResponse().getContentAsString(), UserDto.class);

        // assertions 2nd people
        assertEquals("R2-D2", response200_2.getName());
        assertEquals(83, response200_2.getAge());
        assertEquals(96, response200_2.getHeightInCm());

        wireMock.verify(HttpMethod.GET, "/people/" + PEOPLE_ID, 3);
    }
}
