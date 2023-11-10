package pro.sky.animalsheltertelegrambot.telegram_bot.service.InteractionService;

import com.pengrad.telegrambot.model.CallbackQuery;


/*
Этот класс служит прослойкой между Telegram API и логикой приложения, публикуя события в системе событий Spring.
Таким образом, можно легко расширять логику обработки без изменения основного кода обработчика.
 */
public interface InteractionService {

    public void processCallback(CallbackQuery callbackQuery);
}
