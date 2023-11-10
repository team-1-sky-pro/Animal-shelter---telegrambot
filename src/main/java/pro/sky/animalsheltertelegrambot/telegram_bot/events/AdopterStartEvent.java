package pro.sky.animalsheltertelegrambot.telegram_bot.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;


@Getter
public class AdopterStartEvent extends ApplicationEvent {

    private final Long chatId;
    private final String username;

    public AdopterStartEvent(Object source, Long chatId, String username) {
        super(source);
        this.chatId = chatId;
        this.username = username;
    }
}
