package edu.java.scrapper.client;

import edu.java.scrapper.dto.stackoverflow.QuestionsResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface StackOverflowClient {
    @GetExchange("/2.3/questions/{questionId}?order=desc&sort=activity&site=stackoverflow")
    QuestionsResponse getLastModificationTime(@PathVariable long questionId);

    @GetExchange("/2.3/questions/{questionId}?order=desc&sort=activity&site=stackoverflow")
    HttpStatusCode testUrl(@PathVariable long questionId);
}
