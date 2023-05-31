package dev.courses.springdemo.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class WireMockServerFacade {

    // 1 sec is not enough, some tests are failing
    private static final Duration DEFAULT_DELAY = Duration.of(2, ChronoUnit.SECONDS);
    public static final int DEFAULT_WIRE_MOCK_SERVER_PORT = 9099;
    private WireMockServer wiremockServer;
    private MappingBuilder mappingBuilder;

    public WireMockServer getServer() {
        return wiremockServer;
    }

    public void startServer(int port) {
        wiremockServer = new WireMockServer(wireMockConfig()
                .port(port)
                .extensions(new ResponseTemplateTransformer(true)));
        wiremockServer.start();

        // delay the start of tests to let the time to wiremock server to start
        try {
            Thread.sleep(DEFAULT_DELAY.toMillis());
        } catch (InterruptedException ignored) {
        }
    }

    public void startServer() {
        startServer(DEFAULT_WIRE_MOCK_SERVER_PORT);
    }

    public void stopServer() {
        wiremockServer.stop();
    }

    public void resetStubs(int port) {
        // reset stubs
        (new RestTemplate()).delete(format("http://localhost:%s/__admin/mappings", port));
        // reset request logs to use correctly verify()
        (new RestTemplate()).delete(format("http://localhost:%s/__admin/requests", port));
    }

    public void resetStubs() {
        resetStubs(DEFAULT_WIRE_MOCK_SERVER_PORT);
    }

    public StubResolver when(HttpMethod httpMethod, String url) {
        mappingBuilder = WireMock.request(httpMethod.name(), urlPathEqualTo(url));
        return new StubResolver();
    }

    public class StubResolver {

        public StubResolver withBodyRequest(String jsonStringBodyRequest) {
            mappingBuilder.withRequestBody(equalToJson(jsonStringBodyRequest));
            return this;
        }

        public StubResolver withQueryParams(Map<String, String> queryParameters) {
            if (!CollectionUtils.isEmpty(queryParameters)) {
                for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                    mappingBuilder.withQueryParam(entry.getKey(), equalTo(entry.getValue()));
                }
            }
            return this;
        }

        public void thenRespondJsonFile(HttpStatus httpStatusResponse, String jsonResponseFilePath) {
            mappingBuilder.willReturn(baseResponse(httpStatusResponse)
                    .withBodyFile(jsonResponseFilePath)
            );
            wiremockServer.stubFor(mappingBuilder);
        }

        public void thenRespondJsonString(HttpStatus httpStatusResponse, String jsonString) {
            mappingBuilder.willReturn(baseResponse(httpStatusResponse)
                    .withBody(jsonString)
            );
            wiremockServer.stubFor(mappingBuilder);
        }

        public void thenRespondWithoutBody(HttpStatus httpStatusResponse) {
            mappingBuilder.willReturn(baseResponse(httpStatusResponse));
            wiremockServer.stubFor(mappingBuilder);
        }

        private static ResponseDefinitionBuilder baseResponse(HttpStatus httpStatusResponse) {
            return aResponse()
                    .withStatus(httpStatusResponse.value())
                    .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }
    }

    public void verify(HttpMethod httpMethod, String url, int times) {
        switch (httpMethod.name()) {
            case "POST" -> wiremockServer.verify(times, postRequestedFor(urlEqualTo(url)));
            case "GET" -> wiremockServer.verify(times, getRequestedFor(urlEqualTo(url)));
            case "PUT" -> wiremockServer.verify(times, putRequestedFor(urlEqualTo(url)));
            case "DELETE" -> wiremockServer.verify(times, deleteRequestedFor(urlEqualTo(url)));
            default -> throw new IllegalArgumentException("HTTP method not supported");
        }
    }

}
