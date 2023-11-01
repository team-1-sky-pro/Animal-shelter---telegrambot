package pro.sky.animalsheltertelegrambot.telegram_bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.AdoptionRepository;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;
import pro.sky.animalsheltertelegrambot.service.CommandService;

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

    private final CommandService commandService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, CommandService commandService) {
        this.telegramBot = telegramBot;
        this.commandService = commandService;
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdoptionRepository adoptionRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.callbackQuery() != null) {
                commandService.receivedCallbackMessage(update.callbackQuery());
            }

            if (update.message() != null) {
                Long userId = update.message().chat().id();
                String userName = update.message().chat().firstName();
                String messageReceived = update.message().text();
                if ((messageReceived).equals("/start")) {
                    checkUserStatus(userId, userName);
                }
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
    private void startMessageReceived(Long chatId, String firstName) {
        String responseMessage = "Привет, " + firstName;
        sendMessage(chatId, responseMessage);
    }

    /**
     * метод проверяет это новый user, или уже в БД, является ли он усыновителем или нет,
     * активный или нет
     */
    private void checkUserStatus(Long userId, String userName) {
        User user = new User(userId, userName, false);
        if (userRepository.findById(userId).isPresent()) {
            if (adoptionRepository.findIdByUserId(userId) != null) {
                if (adoptionRepository.checkAdoptionsIsActive(userId) == true) {
                    startMessageReceived(userId, userName + " - is active adopter");
                } else {
                    startMessageReceived(userId, userName + " - is not active adopter");
                }
            } else {
                telegramBot.execute(commandService.executeStartCommandIfUserExists(userId));
            }
        } else if (!userRepository.findById(userId).equals(user)) {
            userRepository.save(user);
            telegramBot.execute(commandService.executeStartCommandIfUserExists(userId));
        }
    }
}
