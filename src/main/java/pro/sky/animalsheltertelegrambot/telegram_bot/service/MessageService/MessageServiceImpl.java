package pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.UserService;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.AdopterStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.RegularUserStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.VolunteerStartEvent;


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
        String username = message.from().username(); // Получаем username из объекта Message
        log.info("Получено текстовое сообщение от {}: {}", username != null ? username : chatId, text);

        if (text != null && "/start".equals(text)) {
            String effectiveUsername = username != null && !username.isEmpty() ? username : "defaultUsername";
            if (isAdopter(chatId)) {
                eventPublisher.publishEvent(new AdopterStartEvent(this, chatId, effectiveUsername));
            } else if (isVolunteer(chatId)) {
                eventPublisher.publishEvent(new VolunteerStartEvent(this, chatId, effectiveUsername));
            } else {
                eventPublisher.publishEvent(new RegularUserStartEvent(this, chatId, effectiveUsername));
            }
            log.info("Инициировано событие /start для пользователя {}", effectiveUsername);
        } else {
            adoptionService.processContactInfo(chatId, text, telegramBot);
            log.info("Текст сообщения передан на дальнейшую обработку: {}", text);
        }
    }

    private boolean isAdopter(Long chatId) {
        return userService.checkIfUserIsAdopter(chatId);
    }

    private boolean isVolunteer(Long chatId) {
        return false;
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
