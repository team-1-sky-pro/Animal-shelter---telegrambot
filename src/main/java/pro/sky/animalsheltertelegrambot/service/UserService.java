package pro.sky.animalsheltertelegrambot.service;

import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.telegram_bot.events.StartCommandEvent;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса для работы с User-ами - базовые CRUD операции
 * добавление
 * вывод (просмотр) по id
 * обновление
 * удаление
 * вывод (просмотр) всех
 * получить список всех волонтеров
 * @author SyutinS
 */

public interface UserService {
    User addUser(User user);
    Optional<User> getUser(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    Collection<User> getAllUsers();

    public boolean hasContactInfo(Long userId);

    public boolean checkIsUserIsNew(Long userId);

    public boolean checkIfUserIsAdopter(Long userId);

    public void handleStart(Long chatId, String userName);

    public void onApplicationEvent(StartCommandEvent event);

    public User findOrCreateUser(Long chatId, String userName);

}
