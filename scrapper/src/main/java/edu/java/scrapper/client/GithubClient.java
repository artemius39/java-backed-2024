package edu.java.scrapper.client;

import edu.java.scrapper.model.github.RepositoryResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface GithubClient {
    @GetExchange("/repos/{repositoryOwner}/{repositoryName}")
    RepositoryResponse getLastUpdateTime(@PathVariable String repositoryOwner, @PathVariable String repositoryName);
}
