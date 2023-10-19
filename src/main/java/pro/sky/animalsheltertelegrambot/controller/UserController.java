package pro.sky.animalsheltertelegrambot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.service.UserService;

import java.util.Collection;

/**
 * Контроллер User (Пользователей всех включая волонтеров) -
 * - для отслеживания/проверки/дополниния/изменения данных волонтерами ручками
 * Позволяет получить по запросу конкретного пользователя по id
 * получает список всех пользователей
 * добавляет пользователя (user)
 * изменяет пользователя (user)
 * удаляет ненужного пользователя (user)
 * @author SyutinS
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User existUser = userService.getUser(id);
        if (existUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existUser);
    }

    @GetMapping("/all")
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody User user) {
        User changeUser = userService.updateUser(id, user);
        if (changeUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(changeUser);
    }

    @DeleteMapping("{id}")
    public void removeUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
