package pro.sky.animalsheltertelegrambot.telegram_bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.AdoptionRepository;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService;

import java.util.List;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.APPLICATION;

/**
 * Сервис, который держит соединение с ботом и постоянно принимает входящие от пользователей сообщения
 *
 * @author Rnd-mi
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final CommandService commandService;
    private final UserRepository userRepository;
    private final AdoptionRepository adoptionRepository;
    private final AdoptionService adoptionService;


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Processing update: {}", update);

            if (update.callbackQuery() != null) {
                handleCallback(update.callbackQuery());
            } else if (update.message() != null) {
                handleMessage(update.message());
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleMessage(Message message) {
        Long userId = message.chat().id();
        String userName = message.chat().firstName();
        String text = message.text();

        if (text != null) {
            if (text.equals("/start")) {
                handleStartCommand(userId, userName);
            } else {
                adoptionService.processContactInfo(userId, text, telegramBot);
            }
        }

        if (message.photo() != null && message.caption() != null) {
            commandService.saveReport(message);
        }
    }



    private void handleCallback(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();
        if (callbackData.equals(APPLICATION.toString())) {
            adoptionService.requestContactInfo(chatId, telegramBot);
        } else {
            commandService.receivedCallbackMessage(callbackQuery);
        }
    }

    private void sendMessage(Long chatId, String sendingMessage) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), sendingMessage);
        telegramBot.execute(sendMessage);
    }

    private void handleStartCommand(Long userId, String userName) {
        if (checkIsUserIsNew(userId)) {
            saveNewUser(userId, userName);
            startMessageReceived(userId, userName + " - new User");
            // Теперь, когда мы приветствовали нового пользователя, давайте спросим его контактные данные.
            adoptionService.requestContactInfo(userId, telegramBot);
        } else if (checkIfUserIsAdopter(userId)) {
            commandService.runMenuForAdopter(userId);
        } else {
            telegramBot.execute(commandService.executeStartCommandIfUserExists(userId));
        }
    }

    /**
     * method to sent welcome message if "/start" command was sent
     */
    private void startMessageReceived(Long chatId, String firstName) {
        String responseMessage = "Привет, " + firstName;
        sendMessage(chatId, responseMessage);
    }

    /**
     * Производится проверка, является ли пользователь усыновителем или нет.
     *
     * @param userId chat ID, который также является ID пользователя
     * @return true - если пользователь является усыновителем, false - если нет
     */
    private boolean checkIfUserIsAdopter(Long userId) {
        return adoptionRepository.checkAdoptionsIsActive(userId).orElse(false);
    }

    /**
     * Сохранение нового пользователя в БД.
     *
     * @param userId   chat ID, который также является ID пользователя
     * @param userName имя пользователя
     */
    private void saveNewUser(Long userId, String userName) {
        User user = new User(userId, userName, false);
        userRepository.save(user);
    }

    /**
     * Проверка на существование пользователя в БД.
     *
     * @param userId chat ID, который также является ID пользователя
     * @return true - если пользователя еще нет в базе, false - если есть
     */
    private boolean checkIsUserIsNew(Long userId) {
        return userRepository.findById(userId).isEmpty();
    }
}
