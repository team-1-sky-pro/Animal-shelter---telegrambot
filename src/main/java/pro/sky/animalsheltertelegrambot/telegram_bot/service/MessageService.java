package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.model.Message;

public interface MessageService {

    public void handleMessage(Message message);
}
