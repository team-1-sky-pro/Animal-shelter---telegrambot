package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.StartCommandEvent;


/**
 * Сервис для обработки входящих текстовых сообщений и фотографий.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final TelegramBot telegramBot;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Обрабатывает входящее сообщение от пользователя.
     *
     * @param message сообщение от пользователя
     */


    public void handleMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();
        String username = message.from().username(); // Получаем username из объекта Message
        log.info("Обработка сообщения от пользователя: {}", chatId);

        if (text != null && "/start".equals(text)) {
            // Проверяем, что username не null и не пустой
            if (username == null || username.isEmpty()) {
                username = "defaultUsername";  // Вы можете установить значение по умолчанию
            }
            eventPublisher.publishEvent(new StartCommandEvent(chatId, username));
            log.info("Обработка события /start от пользователя: {}", chatId);
        } else {
            // ... обработка других сообщений ...
        }
    }


    //метод для отправки *.pdf файла юзеру
    public void sendDocument(String path, Long chatId) {
        SendDocument sendDocument = new SendDocument(chatId, new java.io.File(path));
        telegramBot.execute(sendDocument);
    }
}
