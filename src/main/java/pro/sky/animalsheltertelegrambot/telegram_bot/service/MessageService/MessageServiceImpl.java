package pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
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
    private final AdoptionService adoptionService;

    /**
     * Обрабатывает входящее сообщение от пользователя.
     *
     * @param message сообщение от пользователя
     */


    public void handleMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();
        String username = message.from().username(); // Получаем username из объекта Message
        log.info("Обработка сообщения в методе handleMessage от пользователя: {}", chatId);

        if (text != null && "/start".equals(text)) {
            if (username == null || username.isEmpty()) {
                username = "defaultUsername";
            }
            eventPublisher.publishEvent(new StartCommandEvent(chatId, username));
            log.info("Обработка события /start от пользователя: {}", chatId);
        } else {
            // Все текстовые сообщения, которые не являются командой /start, отправляем в processContactInfo
            adoptionService.processContactInfo(chatId, text, telegramBot);
            log.info("Передача текста в метод processContactInfo от пользователя: {}", chatId);
        }
    }



    //метод для отправки *.pdf файла юзеру
    public void sendDocument(String path, Long chatId) {
        SendDocument sendDocument = new SendDocument(chatId, new java.io.File(path));
        telegramBot.execute(sendDocument);
    }
}
