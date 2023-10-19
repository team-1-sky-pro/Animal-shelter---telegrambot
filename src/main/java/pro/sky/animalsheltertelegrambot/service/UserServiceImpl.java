package pro.sky.animalsheltertelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

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

    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAllById();
    }

    @Override
    public Collection<User> getAllVolunteer() {
        return userRepository.findAllByVolunteerIsTrue();
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
