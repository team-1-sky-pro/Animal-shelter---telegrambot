package pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.ReportService;
import pro.sky.animalsheltertelegrambot.service.UserService;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.AdopterStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.RegularUserStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.ReportStartEvent;


/**
 * Сервис для обработки входящих текстовых сообщений и фотографий от пользователей Telegram бота.
 * Осуществляет первичную обработку и распределение сообщений в зависимости от их содержания.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final TelegramBot telegramBot;
    private final ApplicationEventPublisher eventPublisher;
    private final AdoptionService adoptionService;
    private final UserService userService;

    /**
     * Обрабатывает входящее текстовое сообщение от пользователя.
     * Если сообщение является командой /start, инициирует соответствующее событие.
     * Иначе передает текст сообщения в сервис обработки информации для усыновления.
     *
     * @param message Входящее сообщение от пользователя.
     */
    @Override
    public void handleMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();
        String caption = message.caption();
        PhotoSize[] fileId = message.photo();
        String username = message.from().username();
        log.info("Получено текстовое сообщение от {}: {}", username != null ? username : chatId, text);

        if (text != null || caption !=null) {
            if ("/start".equals(text)) {
                handleStartCommand(chatId, username);
            } else if (isReportMessage(caption, fileId)) {
                // Обработка сообщения, которое является началом отчета
                eventPublisher.publishEvent(new ReportStartEvent(this, chatId, caption, fileId));
                log.info("Создано событие для начала процесса отчета для chatId: {}", chatId);
            } else {
                // Обработка сообщения, которое не является ни командой /start, ни началом отчета
                adoptionService.processContactInfo(chatId, text, telegramBot);
            }
        } else {
            log.info("Сообщение без текста от пользователя с userId: {}.", chatId);
            // Здесь может быть логика обработки сообщений без текста, например, фото или стикеров
        }
    }

    private void handleStartCommand(Long chatId, String username) {
        String effectiveUsername = username != null && !username.isEmpty() ? username : "defaultUsername";
        if (isAdopter(chatId)) {
            eventPublisher.publishEvent(new AdopterStartEvent(this, chatId, effectiveUsername));
        } else {
            eventPublisher.publishEvent(new RegularUserStartEvent(this, chatId, effectiveUsername));
        }
        log.info("Инициировано событие /start для пользователя {}", effectiveUsername);
    }

    private boolean isAdopter(Long chatId) {
        return userService.checkIfUserIsAdopter(chatId);
    }

    private boolean isReportMessage(String caption, PhotoSize[] photoSizes) {
        boolean matchesPetIdFormat = caption != null && caption.matches("^\\d+\\..*");
        boolean hasPhoto = photoSizes != null && photoSizes.length > 0;
        return matchesPetIdFormat && hasPhoto;
    }



    /**
     * Отправляет документ пользователю в Telegram.
     * Документ может быть, например, в формате PDF и содержать информацию об усыновлении или правилах приюта.
     *
     * @param path Путь к файлу документа.
     * @param chatId Идентификатор чата пользователя в Telegram.
     */
    @Override
    public void sendDocument(String path, Long chatId) {
        SendDocument sendDocument = new SendDocument(chatId, new java.io.File(path));
        SendResponse response = telegramBot.execute(sendDocument);
        if (response.isOk()) {
            log.info("Документ {} успешно отправлен пользователю с chatId: {}", path, chatId);
        } else {
            log.error("Ошибка отправки документа {}: {}", path, response.description());
        }
    }

}
