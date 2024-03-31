package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.configuration.ClientConfiguration;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import edu.java.scrapper.exception.ApiException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.Assert.assertThrows;

@WireMockTest(httpPort = 8080)
public class ScrapperClientTest {
    private final ClientConfiguration configuration;

    {
        configuration = new ClientConfiguration();
        ClientConfiguration.ClientConfig config = new ClientConfiguration.ClientConfig();
        config.setBaseUrl("http://localhost:8080");
        configuration.setClients(Map.of("scrapper", config));
    }

    @Test
    void errorsAreHandled() {
        ScrapperClient client = configuration.scrapperClient();
        stubFor(post("/updates").willReturn(jsonResponse("""
                                {
                                    "description": "stub exception",
                                    "code": "400",
                                    "exceptionName": "StubException",
                                    "exceptionMessage": "stub exception",
                                    "stackTrace": []
                                }""", 400)));
        LinkUpdateRequest request = new LinkUpdateRequest(0, null, "", List.of());

        assertThrows(ApiException.class, () -> client.sendUpdate(request));
    }
}
