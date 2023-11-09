package pro.sky.animalsheltertelegrambot.service.impl;

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
import pro.sky.animalsheltertelegrambot.telegram_bot.events.StartCommandEvent;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.CommandService;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.MessageSendingService;

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
 * @author SyutinS
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final MessageSendingService messageSendingService;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    public User addUser(User user) {
        log.info("Was invoked method " + getMethodName());
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
     * Находит существующего пользователя по chatId или создает нового, если такой не найден.
     *
     * @param chatId идентификатор чата пользователя в Telegram
     * @return найденный или созданный пользователь
     */
    @Override
    public User findOrCreateUser(Long chatId, String userName) {
        log.info("Поиск пользователя с chatId: {}", chatId);
        return userRepository.findById(chatId)
                .orElseGet(() -> {
                    log.info("Создание нового пользователя с chatId: {} и username: {}", chatId, userName);
                    User newUser = new User(chatId, userName, false);
                    log.info("Новый пользователь создан с chatId: {} и username: {}", chatId, userName);
                    // Установите другие начальные свойства newUser по мере необходимости
                    return userRepository.save(newUser);
                });
    }

    @Override
    @EventListener
    public void onApplicationEvent(StartCommandEvent event) {
        log.info("Получено событие /start для chatId: {}", event.getChatId());
        handleStart(event.getChatId(), event.getUsername());
        log.info("Событие /start обработано для chatId: {}", event.getChatId());
    }

    /**
     * Обрабатывает начальную команду "/start" от пользователя.
     *
     * @param chatId идентификатор чата пользователя в Telegram
     */
    public void handleStart(Long chatId, String userName) {
        log.info("Обработка команды /start для chatId: {}", chatId);
        Optional<User> userOptional = userRepository.findById(chatId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (checkIfUserIsAdopter(user.getId())) {
                log.info("Пользователь chatId: {} уже существует. Пользователь является усыновителем.", chatId);
                messageSendingService.sendMessage(chatId, "Меню усыновителя");
            } else {
                log.info("Пользователь chatId: {} уже существует. Не усыновитель.", chatId);
            }
        } else {
            log.info("Создание нового пользователя с chatId: {} и userName: {}", chatId, userName);
            User newUser = new User(chatId, userName, false);
            userRepository.save(newUser);
            eventPublisher.publishEvent(new StartCommandEvent(chatId, userName));
        }
    }


}
