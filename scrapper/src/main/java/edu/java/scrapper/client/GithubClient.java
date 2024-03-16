package edu.java.scrapper.client;

import edu.java.scrapper.dto.github.RepositoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface GithubClient {
    @GetExchange("/repos/{repositoryOwner}/{repositoryName}")
    RepositoryResponse getLastUpdateTime(@PathVariable String repositoryOwner, @PathVariable String repositoryName);

    @GetExchange("/repos/{repositoryOwner}/{repositoryName}")
    HttpStatus testUrl(@PathVariable String repositoryOwner, @PathVariable String repositoryName);
}
