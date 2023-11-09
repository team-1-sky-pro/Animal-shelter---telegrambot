package pro.sky.animalsheltertelegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.UserNotFoundException;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.AdoptionRepository;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.UserService;

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
    private final AdoptionService adoptionService;
    private final TelegramBot telegramBot;

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
    public User findOrCreateUser(Long chatId) {
        log.info("Поиск пользователя с chatId: {}", chatId);
        return userRepository.findById(chatId)
                .orElseGet(() -> {
                    log.info("Создание нового пользователя с chatId: {}", chatId);
                    User newUser = new User(chatId, null, false);
                    // Установите другие начальные свойства newUser по мере необходимости
                    return userRepository.save(newUser);
                });
    }

    // UserService
    /**
     * Обрабатывает начальную команду "/start" от пользователя.
     *
     * @param chatId идентификатор чата пользователя в Telegram
     */
    public void handleStart(Long chatId) {
        log.info("Обработка команды /start для chatId: {}", chatId);
        Optional<User> userOptional = userRepository.findById(chatId);
        if (userOptional.isPresent()) {
            // Пользователь существует, проверяем его статус усыновления
            Optional<Boolean> isAdopter = adoptionRepository.checkAdoptionsIsActive(chatId);
            if (isAdopter.isPresent() && isAdopter.get()) {
                log.info("Пользователь chatId: {} является усыновителем. Показ меню усыновителя.", chatId);
                // Показать меню усыновителя
            } else {
                log.info("Пользователь chatId: {} существует, но не является усыновителем. Показ основного меню.", chatId);
                // Показать основное меню
            }
        } else {
            log.info("Пользователь chatId: {} новый. Запрос контактной информации.", chatId);
            // Создание нового пользователя
            User newUser = new User(chatId, null, false); // Или другие начальные свойства
            userRepository.save(newUser);
            // Запрос контактной информации
            adoptionService.requestContactInfo(chatId, telegramBot);
        }
    }


}
