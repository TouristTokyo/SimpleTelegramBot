package com.example.SpringTgBot.service;

import com.example.SpringTgBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start":
                    String userName = update.getMessage().getChat().getUserName();
                    sendMessage(chatId, userName);
                    break;
                default:
                    sendMessage(chatId, "Sorry, I did not understand you.");
            }
        }
    }

    private void sendMessage(long chatId, String userName) {
        log.info("Send message to "+userName);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hi, " + userName + "!");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error: " + e.getMessage());
        }
    }
}
