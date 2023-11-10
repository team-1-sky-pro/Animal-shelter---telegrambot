package pro.sky.animalsheltertelegrambot.telegram_bot.service.MediaService;

import com.pengrad.telegrambot.model.Message;

/*
Этот сервис занимается обработкой медиа-сообщений, таких как фото, которые пользователи отправляют вашему боту.
Он может включать в себя сохранение данных в базу, обработку изображений или другую медиа-специфичную логику.
 */
public interface MediaService {

    public void processPhotoMessage(Message message);
}
