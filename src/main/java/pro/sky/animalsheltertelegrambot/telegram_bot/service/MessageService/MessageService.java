package pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageService;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;

/**
 * Сервис для обработки входящих текстовых сообщений и фотографий от пользователей Telegram бота.
 * Осуществляет первичную обработку и распределение сообщений в зависимости от их содержания.
 */
public interface MessageService {

    void handleMessage(Message message);

    //метод для отправки *.pdf файла юзеру
    void sendDocument(String path, Long chatId);
}
