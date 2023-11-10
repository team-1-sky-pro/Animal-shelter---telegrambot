package pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки сообщений через Telegram бота.
 * Позволяет отправлять простые текстовые сообщения и сложные структурированные сообщения.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageSendingServiceImpl implements MessageSendingService {

    private final TelegramBot telegramBot;

    /**
     * Отправляет текстовое сообщение пользователю.
     * @param chatId идентификатор чата пользователя в Telegram
     * @param sendingMessage текст сообщения для отправки
     */
    public void sendMessage(Long chatId, String sendingMessage) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), sendingMessage);
        telegramBot.execute(sendMessage);
    }

    /**
     * Отправляет структурированное сообщение SendMessage пользователю.
     * @param sendMessage объект сообщения SendMessage для отправки через Telegram API
     */
    @Override
    public void sendMessage(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }
}
