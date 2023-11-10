package pro.sky.animalsheltertelegrambot.telegram_bot.service.InteractionService;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.CallbackEvent;

/**
 * Сервис для взаимодействия с пользователем в Telegram боте.
 * Отвечает за приём и обработку действий пользователя, инициированных через inline-кнопки.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Обрабатывает колбэк-запросы, полученные от пользователей через inline-кнопки.
     * Использует механизм Spring Events для дальнейшей обработки.
     * @param callbackQuery объект колбэк-запроса, полученный от Telegram API
     */
    public void processCallback(CallbackQuery callbackQuery) {
        log.info("processCallback вызван с параметром: {}", callbackQuery);
        eventPublisher.publishEvent(new CallbackEvent(callbackQuery));
    }
}
