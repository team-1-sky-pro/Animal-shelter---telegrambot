package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.PhotoService;
import pro.sky.animalsheltertelegrambot.service.ReportService;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaService {

    private final TelegramBot telegramBot;
    private final PhotoService photoService;
    private final ReportService reportService;

    /**
     * Обработка сообщения с фото, отправленного пользователем.
     *
     * @param message сообщение с фото
     */
    public void processPhotoMessage(Message message) {
        Long chatId = message.chat().id();
        log.info("Обработка фото сообщения от пользователя {}", chatId);
        // Здесь логика по обработке фото сообщений
        // Например, сохранение фото в базу данных, создание отчета и т.д.
    }
}
