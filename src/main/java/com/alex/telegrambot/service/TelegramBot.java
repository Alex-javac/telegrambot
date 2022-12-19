package com.alex.telegrambot.service;

import com.alex.telegrambot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().toLowerCase().trim();
            if (messageText.contains("а у тебя") || messageText.contains("как жизнь") || messageText.contains("как дела") || messageText.contains("че как") || messageText.contains("а как у тебя")) {
                messageText = "как дела";
            } else if (messageText.contains("норм") || messageText.contains("хорошо") || messageText.contains("отлично") || messageText.contains("лучше всех")) {
                messageText = "норм";
            } else if (messageText.contains("hi") || messageText.contains("hello") || messageText.contains("привет") || messageText.contains("здаров")) {
                messageText = "привет";
            }
            Long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "привет":
                    helloCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "❤️":
                    if(update.getMessage().getChat().getFirstName().equals("Алеся")) {
                        sendMassage(chatId, "Я тебя тоже люблю " + update.getMessage().getChat().getFirstName() + "!");
                    }else {
                        sendMassage(chatId, "\uD83D\uDC4D");
                    }
                    break;
                case "как дела":
                    sendMassage(chatId, "Лучше всех!!! Я же Bot");
                    break;
                case "норм":
                    sendMassage(chatId, "Я очень рад за тебя");
                    break;
                default:
                    sendMassage(chatId, "sorry Я еще мал и не умею общаться на разные темы");
            }
        }
    }

    private void helloCommandReceived(Long chatId, String firstName) {
        String answer = "Очень приятно познакомиться " + firstName + ", как дела?";
        sendMassage(chatId, answer);
    }

    private void startCommandReceived(Long chatId, String firstName) {
        String answer = "Приветствую " + firstName + ", Я приглашаю Вас на День Рождения!";
        log.info("{} used /start", firstName);
        sendMassage(chatId, answer);
    }

    private void sendMassage(Long chatId, String massage) {
        SendMessage message = new SendMessage(String.valueOf(chatId), massage);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("TelegramBot.sendMassage(): ", e);
        }
    }
}
