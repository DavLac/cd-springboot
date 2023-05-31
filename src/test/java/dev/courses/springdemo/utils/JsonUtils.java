package dev.courses.springdemo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

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

    public <T> T toObject(String jsonStringObject, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(jsonStringObject, clazz);
    }

    /**
     * To be used when deserializing a list of objects
     * parameter to pass : new TypeReference<List<MyClass>>(){}
     */
    public <T> T toCollection(String jsonStringObjectList, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return objectMapper.readValue(jsonStringObjectList, valueTypeRef);
    }
}
