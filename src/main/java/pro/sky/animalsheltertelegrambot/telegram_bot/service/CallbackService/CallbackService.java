package pro.sky.animalsheltertelegrambot.telegram_bot.service.CallbackService;

import com.pengrad.telegrambot.model.CallbackQuery;

/*
Класс CallbackService:
Этот класс отвечает за обработку callback-запросов, которые приходят от Telegram после нажатия пользователем inline-кнопок в боте.
Сервис делегирует обработку InteractionService, чтобы обеспечить разделение ответственности и избежать циклических зависимостей.
 */
public interface CallbackService {

    public void processCallback(CallbackQuery callbackQuery);
}
