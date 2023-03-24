package dev.courses.springdemo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

import static com.fasterxml.jackson.databind.cfg.ConstructorDetector.USE_PROPERTIES_BASED;

public class JsonUtils {

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new JavaTimeModule()) // handle time
            .constructorDetector(USE_PROPERTIES_BASED)
            .build();

    public static String toJsonString(final Object obj) {
        try {
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new ParameterNamesModule())
                    .constructorDetector(USE_PROPERTIES_BASED)
                    .build();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T deserializeResult(MvcResult result, Class<T> clazz)
            throws JsonProcessingException, UnsupportedEncodingException {
        String contentAsString = result.getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString, clazz);
    }
}
