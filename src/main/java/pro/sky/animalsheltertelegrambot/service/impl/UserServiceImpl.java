package pro.sky.animalsheltertelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;
import pro.sky.animalsheltertelegrambot.service.UserService;

import java.util.Collection;
import java.util.Optional;

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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
