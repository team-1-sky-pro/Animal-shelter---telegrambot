package pro.sky.animalsheltertelegrambot.telegram_bot.service.CallbackService;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.InteractionService.InteractionService;

/**
 * Сервис для обработки callback-запросов от Telegram.
 * Используется в Telegram боте для обработки действий пользователя, инициированных через inline-кнопки.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final InteractionService interactionService;

    /**
     * Обрабатывает callback-запросы, полученные от inline-кнопок в сообщениях бота.
     * @param callbackQuery объект запроса callback от Telegram API
     */
    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        log.info("Метод processCallback получил запрос на обработку: {}", callbackQuery);
        interactionService.processCallback(callbackQuery);
    }
}

