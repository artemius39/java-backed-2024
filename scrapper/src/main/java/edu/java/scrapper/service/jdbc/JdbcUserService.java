package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.model.User;
import edu.java.scrapper.repository.JdbcUserRepository;
import edu.java.scrapper.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JdbcUserService implements UserService {
    private final JdbcUserRepository userRepository;

    @Override
    public void register(long chatId) {
        userRepository.add(new User(chatId));
    }

    @Override
    public void unregister(long chatId) {
        userRepository.remove(new User(chatId));
    }
}
