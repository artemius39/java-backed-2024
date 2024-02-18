package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient(@Value("https://api.github.com") String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(GithubClient.class);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(@Value("https://api.stackexchange.com") String baseUrl) {
        HttpClient httpClient = HttpClient.create()
            .baseUrl(baseUrl)
            .compress(true);

        WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(StackOverflowClient.class);
    }
}
