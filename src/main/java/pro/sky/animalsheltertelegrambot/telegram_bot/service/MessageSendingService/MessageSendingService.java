package pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService;

import com.pengrad.telegrambot.request.SendMessage;

/*
Этот класс обеспечивает функциональность по отправке текстовых сообщений пользователям Telegram.
Он инкапсулирует логику создания объекта SendMessage и его отправки через экземпляр TelegramBot.
 */
public interface MessageSendingService {

    public void sendMessage(Long chatId, String sendingMessage);
    public void sendMessage(SendMessage sendMessage);
}
