package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.CommandService;

/**
 * Сервис для обработки входящих текстовых сообщений и фотографий.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final TelegramBot telegramBot;
    private final CommandService commandService;
    private final AdoptionService adoptionService;

    /**
     * Обрабатывает входящее сообщение от пользователя.
     *
     * @param message сообщение от пользователя
     */
    public void handleMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();
        log.info("Обработка сообщения от пользователя: {}", chatId);

        if (text != null) {
            log.info("Текстовое сообщение от пользователя {}: {}", chatId, text);
            if (text.equals("/start")) {
                commandService.processStartCommand(chatId, message.chat().firstName());
            } else {
                adoptionService.processContactInfo(chatId, text, telegramBot);
            }
        } else if (message.photo() != null && message.caption() != null) {
            log.info("Сообщение с фото от пользователя {}: {}", chatId, message.caption());
            commandService.processPhotoMessage(message);
        } else {
            log.warn("Получено сообщение без текста и фото от пользователя {}", chatId);
        }
    }

    //метод для отправки *.pdf файла юзеру
    public void sendDocument(String path, Long chatId) {
        SendDocument sendDocument = new SendDocument(chatId, new java.io.File(path));
        telegramBot.execute(sendDocument);
    }
}
