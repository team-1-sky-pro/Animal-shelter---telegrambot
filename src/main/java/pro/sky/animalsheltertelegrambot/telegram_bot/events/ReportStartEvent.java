package pro.sky.animalsheltertelegrambot.telegram_bot.events;

import com.pengrad.telegrambot.model.PhotoSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;


@Getter
public class ReportStartEvent extends ApplicationEvent {

    private final Long chatId;
    private final String text;
    private final PhotoSize[] fileId;

    public ReportStartEvent(Object source, Long chatId, String text, PhotoSize[] fileId) {
        super(source);
        this.chatId = chatId;
        this.text = text;
        this.fileId = fileId;
    }
}
