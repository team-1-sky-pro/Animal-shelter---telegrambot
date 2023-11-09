package pro.sky.animalsheltertelegrambot.telegram_bot.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartCommandEvent {

    private final Long chatId;
    private final String username;

}
