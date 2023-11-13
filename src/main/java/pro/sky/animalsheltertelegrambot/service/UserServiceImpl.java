package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.UserNotFoundException;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.AdoptionRepository;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;
import pro.sky.animalsheltertelegrambot.service.UserService;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.AdopterStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.RegularUserStartEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService.CommandService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService.MessageSendingService;

import java.util.Collection;
import java.util.Optional;

import static pro.sky.animalsheltertelegrambot.utils.MethodNameRetriever.getMethodName;

/**
 * Реализация работы сервиса базовый CRUD для - User
 * добавление
 * просмотр
 * получить список всех волонтеров
 * изменение
 * удаление
 *
 * @author SyutinS
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final MessageSendingService messageSendingService;
    private final CommandService commandService;

    /**
     * Добавляет нового пользователя в репозиторий.
     *
     * @param user объект пользователя для сохранения
     * @return сохраненный пользователь
     */
    @Override
    public User addUser(User user) {
        log.info("Добавлен новый пользователь с ID: {}", user.getId());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        log.info("Was invoked method " + getMethodName());
        findOrThrow(id);
        return userRepository.findById(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Was invoked method " + getMethodName());
        return userRepository.findAll();
    }

    @Override
    public boolean hasContactInfo(Long userId) {
        return userRepository.existsByIdAndEmailIsNotNullAndPhoneIsNotNull(userId);
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Was invoked method " + getMethodName());
        findOrThrow(id);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Was invoked method " + getMethodName());
        findOrThrow(id);
        userRepository.deleteById(id);
    }

    private void findOrThrow(Long id) {
        log.info("Was invoked method " + getMethodName());
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.error("User with id = {} not exist", id);
            throw new UserNotFoundException(" User with id not found");
        }
    }

    public void saveNewUser(Long userId, String userName) {
        User user = new User(userId, userName, false);
        userRepository.save(user);
    }


    /*
    =============================additional methods==============================================================
    дополнительная логика для работы с пользователями в системе усыновления животных.
     */


    /**
     * Проверка на существование пользователя в БД.
     *
     * @param userId chat ID, который также является ID пользователя
     * @return true - если пользователя еще нет в базе, false - если есть
     */
    public boolean checkIsUserIsNew(Long userId) {
        return userRepository.findById(userId).isEmpty();
    }

    /**
     * Производится проверка, является ли пользователь усыновителем или нет.
     *
     * @param userId chat ID, который также является ID пользователя
     * @return true - если пользователь является усыновителем, false - если нет
     */
    public boolean checkIfUserIsAdopter(Long userId) {
        return adoptionRepository.checkAdoptionsIsActive(userId).orElse(false);
    }


    /**
     * Обрабатывает событие /start, которое генерируется, когда пользователь начинает взаимодействие с ботом.
     * Этот метод инициирует процесс регистрации пользователя или представляет меню, если пользователь уже зарегистрирован.
     *
     * @param event Событие, содержащее информацию о пользователе и его чате.
     */
    @EventListener
    private void onApplicationEventUser(RegularUserStartEvent event) {
        log.info("Начало обработки команды /start для chatId: {}", event.getChatId());
        handleStart(event.getChatId(), event.getUsername());
    }

    @EventListener
    private void onAdopterStartEventAdoption(AdopterStartEvent event) {
        log.info("Обработка AdopterStartEvent для chatId: {}", event.getChatId());
        commandService.runMenuForAdopter(event.getChatId(), event.getUsername());
    }

    /**
     * Обрабатывает команду /start, регистрируя нового пользователя или предоставляя меню существующему.
     *
     * @param chatId   Идентификатор чата пользователя в Telegram.
     * @param userName Имя пользователя в Telegram.
     */
    @Override
    public void handleStart(Long chatId, String userName) {
        log.info("Обработка /start для chatId: {}", chatId);

        Optional<User> existingUser = userRepository.findById(chatId);
        if (existingUser.isEmpty()) {
            createUser(chatId, userName);
            sendWelcomeMessage(chatId, userName);
        }
    }

    private void sendWelcomeMessage(Long chatId, String userName) {
        String messageText = "Привет, " + userName + "! Добро пожаловать в нашу систему.";
        messageSendingService.sendMessage(chatId, messageText);
    }

    /**
     * Создает нового пользователя и сохраняет его в репозитории.
     *
     * @param chatId   Идентификатор чата пользователя в Telegram.
     * @param userName Имя пользователя в Telegram.
     * @return Созданный пользователь.
     */
    private void createUser(Long chatId, String userName) {
        log.info("Создание нового пользователя с chatId: {} и userName: {}", chatId, userName);
        User newUser = new User(chatId, userName, false);
        userRepository.save(newUser);
    }

    @Override
    public void sendVolunteerChat(Long chatId) {
        try {
            Long toChatId = userRepository.findUserIdIfUserIsVolunteer();
            String name = userRepository.findNameById(chatId);
            String phone = userRepository.findPhoneById(chatId);
            String email = userRepository.findEmailById(chatId);

            StringBuilder messageText = new StringBuilder("Привет! \nМеня зовут @").append(name)
                    .append("\nМне нужна помощь волонтера.");

            // Добавляем телефон и email в сообщение, если они доступны
            if (phone != null && !phone.isEmpty()) {
                messageText.append("\nМой телефон: ").append(phone);
            }
            if (email != null && !email.isEmpty()) {
                messageText.append("\nМой email: ").append(email);
            }

            SendMessage sendMessage = new SendMessage(toChatId, messageText.toString());
            messageSendingService.sendMessage(sendMessage);
            messageSendingService.sendMessage(new SendMessage(chatId, "Сообщение отправлено волонтеру. \nОжидайте ответ."));

            log.info("Сообщение отправлено волонтеру с chatId: {}", toChatId);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения волонтеру", e);
        }
    }
}
