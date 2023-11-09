package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageSendingServiceImpl implements MessageSendingService {

    private final TelegramBot telegramBot;

    public void sendMessage(Long chatId, String sendingMessage) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), sendingMessage);
        telegramBot.execute(sendMessage);
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }
}
