package pro.sky.animalsheltertelegrambot.service;

import pro.sky.animalsheltertelegrambot.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса для работы с User-ами - базовые CRUD операции
 * добавление
 * вывод (просмотр) по id
 * обновление
 * удаление
 * вывод (просмотр) всех
 * @author SyutinS
 */

public interface UserService {
    User addUser(User user);
    Optional<User> getUser(Long id);
    public Collection<User> getAllVolunteer();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    Collection<User> getAllUsers();
}
