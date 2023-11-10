package pro.sky.animalsheltertelegrambot.telegram_bot.events;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс события для представления колбэк-запроса от Telegram бота.
 * Это событие генерируется, когда пользователь взаимодействует с inline-клавиатурой.
 */
@Data
@AllArgsConstructor
public class CallbackEvent {

    private final CallbackQuery callbackQuery;


}
