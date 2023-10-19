package pro.sky.animalsheltertelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.User;

import java.util.Collection;

/**
 * Реализация работы сервиса базовый CRUD для - User
 * - далеко не окончательный вариант требует доработки в ближайшем будующем
 * добавление
 * просмотр
 * изменение
 * удаление
 * @author SyutinS
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User getUser(Long id) {
        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User updateUser(Long id, User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

}
