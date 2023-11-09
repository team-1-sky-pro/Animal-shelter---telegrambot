package pro.sky.animalsheltertelegrambot.telegram_bot.service.InteractionService;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.CallbackEvent;


@Service
@Slf4j
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final ApplicationEventPublisher eventPublisher;


    /**
     * Обрабатывает колбэк-запросы, полученные от пользователей через inline-кнопки.
     *
     * @param callbackQuery объект колбэк-запроса, полученный от Telegram API
     */
    public void processCallback(CallbackQuery callbackQuery) {
        // Публикация события вместо прямого вызова метода
        log.info("Информация пришла в метод processCallback");
        eventPublisher.publishEvent(new CallbackEvent(callbackQuery));
    }
}
