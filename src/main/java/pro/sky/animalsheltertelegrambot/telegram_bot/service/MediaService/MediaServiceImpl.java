package pro.sky.animalsheltertelegrambot.telegram_bot.service.MediaService;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.PhotoService;
import pro.sky.animalsheltertelegrambot.service.ReportService;

/**
 * Сервис для обработки медиа-сообщений в Telegram боте.
 * Осуществляет обработку фотографий, присланных пользователями, включая сохранение и анализ контента.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    /**
     * Обрабатывает сообщения с фото, отправленные пользователем в чат бота.
     * @param message сообщение с фото, полученное от пользователя через Telegram API
     */
    public void processPhotoMessage(Message message) {
        Long chatId = message.chat().id();
        log.info("Начата обработка фото сообщения от chatId: {}", chatId);
    }
}




