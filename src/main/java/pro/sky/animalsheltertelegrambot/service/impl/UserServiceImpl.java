package pro.sky.animalsheltertelegrambot.service.impl;

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

    // UserService
    public void handleStart(Long chatId) {
        User user = findOrCreateUser(chatId);
        if (user.isNew()) {
            adoptionService.requestContactInfo(chatId);
        } else if (user.isAdopter()) {
            // Показать меню усыновителя
        } else {
            // Показать основное меню
        }
    }
}
