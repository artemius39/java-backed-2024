package edu.java.scrapper.service.updater;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.dto.github.RepositoryResponse;
import edu.java.scrapper.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GithubRepositoryLinkUpdater implements LinkUpdater {
    private static final Pattern GITHUB_REPOSITORY_PATTERN =
        Pattern.compile("https://github.com/([^/]+)/([^/]+)");

    private final GithubClient githubClient;

    @Override
    public Optional<String> update(URI url, OffsetDateTime lastUpdatedAt) {
        RepositoryInfo info = parseUrl(url);
        assert info != null : "Updating invalid link";

        RepositoryResponse response = githubClient.getLastUpdateTime(info.owner(), info.name());
        if (response.updatedAt().isAfter(lastUpdatedAt)) {
            return Optional.of("В репозитории `%s/%s` произошло обновление".formatted(info.owner(), info.name()));
        }
        return Optional.empty();
    }

    @Override
    public boolean supports(URI url) {
        RepositoryInfo repositoryInfo = parseUrl(url);
        if (repositoryInfo == null) {
            return false;
        }
        return githubClient.testUrl(repositoryInfo.owner(), repositoryInfo.name()).is2xxSuccessful();
    }

    private RepositoryInfo parseUrl(URI url) {
        Matcher matcher = GITHUB_REPOSITORY_PATTERN.matcher(url.toString());
        return matcher.find() ? new RepositoryInfo(matcher.group(1), matcher.group(2)) : null;
    }

    private record RepositoryInfo(String owner, String name) {
    }
}
