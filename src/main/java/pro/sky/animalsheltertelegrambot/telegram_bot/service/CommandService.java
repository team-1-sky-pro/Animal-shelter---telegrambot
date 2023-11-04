package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandService {
    SendMessage executeStartCommandIfUserExists(Long chatId);

    void receivedCallbackMessage(CallbackQuery callbackQuery);

    /**
     * Создает меню выбора действия для пользователя-усыновителя. <p>
     * Возможные действия: <p>
     * - отправить отчет; <p>
     * - выбрать еще одного питомца; <p>
     * - помощь волонтера.
     * @param chatId ID чата с текущим пользователем
     */
    void runMenuForAdopter(Long chatId);
}
