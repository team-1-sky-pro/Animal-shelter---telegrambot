package pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageService;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;

public interface MessageService {

    public default void handleMessage(Message message) {

    }

    //метод для отправки *.pdf файла юзеру
    public void sendDocument(String path, Long chatId);
}
