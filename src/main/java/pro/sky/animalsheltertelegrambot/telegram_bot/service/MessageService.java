package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;

public interface MessageService {

    public void handleMessage(Message message);

    //метод для отправки *.pdf файла юзеру
    public void sendDocument(String path, Long chatId);
}
