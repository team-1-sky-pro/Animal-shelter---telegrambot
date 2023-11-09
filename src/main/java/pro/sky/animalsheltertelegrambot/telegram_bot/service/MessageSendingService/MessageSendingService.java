package pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService;

import com.pengrad.telegrambot.request.SendMessage;

public interface MessageSendingService {

    public void sendMessage(Long chatId, String sendingMessage);

    public void sendMessage(SendMessage sendMessage);
}
