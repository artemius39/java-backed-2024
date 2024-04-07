package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.service.command.Command;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BotServiceImpl implements BotService {
    private final Map<String, Command> commands;
    private final TelegramBot telegramBot;

    public BotServiceImpl(List<Command> commandList, TelegramBot telegramBot
    ) {
        commands = commandList.stream()
                .collect(Collectors.toMap(Command::name, Function.identity()));
        this.telegramBot = telegramBot;
    }

    @Override
    public void processMessage(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        String messageText = message.text();
        String commandName = getCommandName(messageText);
        Command command = commands.get(commandName);
        String responseMessage;
        if (command == null) {
            responseMessage = "Неизвестная команда. Введите /help, чтобы получить список доступных команд";
        } else {
            String commandArguments = getCommandArguments(messageText, commandName);
            responseMessage = command.process(commandArguments, chatId);
        }
        telegramBot.execute(new SendMessage(chatId, responseMessage));
    }

    @Override
    public void sendUpdates(LinkUpdateRequest update) {
        update.tgChatIds().stream()
            .map(id -> new SendMessage(id, update.description()))
            .forEach(telegramBot::execute);
    }

    private String getCommandArguments(String messageText, String commandName) {
        if (commandName.length() == messageText.length()) {
            return messageText;
        } else {
            return messageText.substring(commandName.length() + 1);
        }
    }

    private String getCommandName(String messageText) {
        int indexOf = messageText.indexOf(' ');
        return indexOf == -1 ? messageText : messageText.substring(0, indexOf);
    }
}
