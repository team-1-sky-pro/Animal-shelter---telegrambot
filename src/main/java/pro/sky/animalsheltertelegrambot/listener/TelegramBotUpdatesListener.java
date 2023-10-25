package pro.sky.animalsheltertelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;

import java.util.List;

/**
 * Сервис, который держит соединение с ботом и постоянно принимает входящие от пользователей сообщения
 *
 * @author Rnd-mi
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Autowired
    private UserRepository userRepository;
//    static final String HELP_TEXT = "Привет! Я чат-бот приюта для животных.\n\n" +
//            "Я помогу тебе:\n\n" +
//            "1. найти доброго и преданного друга,\n\n" +
//            "2. расскажу как за ним ухаживать,\n\n" +
//            " Скорее жми ЗАПУСТИТЬ!";

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() == null) {
                logger.info("Null message was sent");
                return;
            }
            Long chatId = update.message().chat().id();
            String userName = update.message().chat().username();
            String messageReceived = update.message().text();
            if (("/start").equals(messageReceived)) {
                saveUserToDB(chatId, userName);
                startMessageReceived(chatId, update.message().chat().firstName());
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String sendingMessage) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), sendingMessage);
        telegramBot.execute(sendMessage);
    }

    /**
     * method to sent welcome message if "/start" command was sent
     */
    private void startMessageReceived(Long chatId, String userName) {
        String responseMessage = "Привет, " + userName;
        sendMessage(chatId, responseMessage);
    }

    private void saveUserToDB(Long chatId, String userName) {
        User user = new User(chatId, userName, false);
        if (!userRepository.findById(chatId).isPresent()) {
            userRepository.save(user);
        }
    }
}
