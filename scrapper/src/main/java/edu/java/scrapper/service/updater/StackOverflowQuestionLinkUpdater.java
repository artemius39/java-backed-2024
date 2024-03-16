package edu.java.scrapper.service.updater;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.stackoverflow.QuestionResponse;
import edu.java.scrapper.dto.stackoverflow.QuestionsResponse;
import edu.java.scrapper.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StackOverflowQuestionLinkUpdater implements LinkUpdater {
    private static final Pattern STACKOVERFLOW_QUESTION_PATTERN =
        Pattern.compile("https://stackoverflow.com/questions/([^/]+)/[^/]+");

    private final StackOverflowClient stackOverflowClient;

    @Override
    public Optional<String> update(URI url, OffsetDateTime lastUpdatedAt) {
        long questionId = parseUrl(url).orElseThrow();

        QuestionsResponse response = stackOverflowClient.getLastModificationTime(questionId);
        QuestionResponse question = response.items().getFirst();
        if (question.lastActivityDate().isAfter(lastUpdatedAt)) {
            return Optional.of("В вопросе `%s` произошло обновление".formatted(url));
        }
        return Optional.empty();
    }

    @Override
    public boolean supports(URI url) {
        OptionalLong id = parseUrl(url);
        if (id.isEmpty()) {
            return false;
        }
        return stackOverflowClient.testUrl(id.getAsLong()).is2xxSuccessful();
    }

    private OptionalLong parseUrl(URI url) {
        Matcher matcher = STACKOVERFLOW_QUESTION_PATTERN.matcher(url.toString());
        try {
            return matcher.find() ? OptionalLong.of(Long.parseLong(matcher.group(1))) : OptionalLong.empty();
        } catch (NumberFormatException e) {
            return OptionalLong.empty();
        }
    }
}
