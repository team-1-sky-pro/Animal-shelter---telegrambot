package pro.sky.animalsheltertelegrambot.telegram_bot.events;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CallbackEvent {

    private final CallbackQuery callbackQuery;


}
