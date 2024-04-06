package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.configuration.ClientConfiguration;
import edu.java.scrapper.dto.github.RepositoryResponse;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8080)
class GithubClientTest {
    private final ClientConfiguration configuration;

    {
        configuration = new ClientConfiguration();
        ClientConfiguration.ClientConfig config = new ClientConfiguration.ClientConfig();
        config.setBaseUrl("http://localhost:8080");
        ClientConfiguration.RetryPolicy policy = new ClientConfiguration.RetryPolicy();
        policy.setType(ClientConfiguration.RetryPolicy.Type.NONE);
        config.setRetryPolicy(policy);
        configuration.setClients(Map.of("github", config));
    }

    @Test
    void timeIsParsedCorrectly() {
        stubFor(get("/repos/owner/repo").willReturn(jsonResponse("{\"updated_at\": \"2011-01-26T19:14:43Z\"}", 200)));
        GithubClient client = configuration.githubClient();
        OffsetDateTime expected = OffsetDateTime.parse("2011-01-26T19:14:43Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        RepositoryResponse response = client.getLastUpdateTime("owner", "repo");

        assertThat(response.updatedAt()).isEqualTo(expected);
    }

    @Test
    void unknownFieldsAreIgnored() {
        stubFor(get("/repos/owner/repo").willReturn(jsonResponse(
            """
                {
                    "name": "name",
                    "updated_at": "2011-01-26T19:14:43Z",
                    "pushed_at": "2011-01-26T19:06:43Z"
                }
                """,
            200
        )));
        GithubClient client = configuration.githubClient();
        OffsetDateTime expected = OffsetDateTime.parse("2011-01-26T19:14:43Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        RepositoryResponse response = client.getLastUpdateTime("owner", "repo");

        assertThat(response.updatedAt()).isEqualTo(expected);
    }
}
