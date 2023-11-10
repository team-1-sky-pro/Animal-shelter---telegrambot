package pro.sky.animalsheltertelegrambot.telegram_bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.CallbackService.CallbackService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageService.MessageService;

import java.util.List;

/**
 * Служба, поддерживающая соединение с телеграм-ботом и непрерывно принимающая входящие сообщения от пользователей.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final MessageService messageService;
    private final CallbackService callbackService;

    /**
     * Инициализация слушателя обновлений для телеграм-бота после создания бина.
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Обрабатывает список обновлений, полученных от телеграм-бота.
     *
     * @param updates список обновлений для обработки
     * @return код статуса, подтверждающий обработанные обновления
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Получено обновление: {}", update);

            if (update.callbackQuery() != null) {
                log.info("Обработка колбэк-запроса от пользователя: {}", update.callbackQuery().from().id());
                callbackService.processCallback(update.callbackQuery());
            } else if (update.message() != null) {
                log.info("Обработка сообщения от пользователя: {}", update.message().chat().id());
                messageService.handleMessage(update.message());
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
