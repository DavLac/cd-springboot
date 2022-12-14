package dev.courses.springdemo.configuration;

import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class WebClientConfig {

    @Value("${application.webclient.read-timeout}")
    private Integer readTimeOut;

    @Value("${application.webclient.connect-timeout}")
    private Integer connectTimeout;

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .tcpConfiguration(tcpClient -> {
                            tcpClient = tcpClient.option(CONNECT_TIMEOUT_MILLIS, connectTimeout);
                            tcpClient = tcpClient.doOnConnected(conn ->
                                    conn.addHandlerLast(new ReadTimeoutHandler(readTimeOut, MILLISECONDS))
                            );
                            return tcpClient;
                        })))
                .build();
    }
}
