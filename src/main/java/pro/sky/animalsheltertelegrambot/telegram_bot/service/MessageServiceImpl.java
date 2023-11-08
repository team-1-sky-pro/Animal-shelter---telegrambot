package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.UserService;


/*
MessageService
Этот сервис будет обрабатывать входящие текстовые сообщения и фото.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {


    private final TelegramBot telegramBot;
    private final CommandService commandService;
    private final AdoptionService adoptionService;

    public void handleMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();

        if (text != null) {
            commandService.processTextMessage(chatId, text);
        } else if (message.photo() != null && message.caption() != null) {
            commandService.processPhotoMessage(message);
        }
    }

    public void handleMessage(Message message) {
        Long userId = message.chat().id();
        String userName = message.chat().firstName();
        String text = message.text();

        if (text != null) {
            if (text.equals("/start")) {
                handleStartCommand(userId, userName);
            } else {
                adoptionService.processContactInfo(userId, text, telegramBot);
            }
        }

        if (message.photo() != null && message.caption() != null) {
            commandService.saveReport(message);
        }
    }

    public void handleMessage(Message message) {
        Long userId = message.chat().id();
        String userName = message.chat().firstName();
        String text = message.text();

        if (text != null) {
            if (text.equals("/start")) {
                handleStartCommand(userId, userName);
            } else {
                adoptionService.processContactInfo(userId, text, telegramBot);
            }
        }

        if (message.photo() != null && message.caption() != null) {
            commandService.saveReport(message);
        }
    }

}
