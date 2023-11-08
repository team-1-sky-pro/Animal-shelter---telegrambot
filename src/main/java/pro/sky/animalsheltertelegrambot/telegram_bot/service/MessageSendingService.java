package pro.sky.animalsheltertelegrambot.telegram_bot.service;

public interface MessageSendingService {

    public void sendMessage(Long chatId, String sendingMessage);
}
