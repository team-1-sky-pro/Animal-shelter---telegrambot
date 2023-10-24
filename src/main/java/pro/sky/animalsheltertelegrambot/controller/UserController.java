package pro.sky.animalsheltertelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.service.UserService;

import java.util.Collection;
import java.util.Optional;

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
@RequestMapping("/users")
@Tag(name = " Пользователи ", description = "Возможные операции (просмотр, изменение, удаление)")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Получение пользователя по id из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = " найден пользователь по id ",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = " пользователь с таким id не найден "
                    )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<Optional<User>> getUser(
            @PathVariable Long id) {
        Optional<User> existUser = userService.getUser(id);
        if (existUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existUser);
    }

    @Operation(
            summary = "Получение всех пользователей"
    )
    @GetMapping("/all")
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * получает список всех волонтеров
     */
//    @GetMapping("/volunteer")
//    public Collection<User> getAllVolunteers() {
//        return userService.getAllVolunteer();
//    }
    @Operation(
            summary = "Добавление нового пользователя в базу",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "пользователь успешно добавлен",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "неправильное заполнение полей пользователя"
                    )
            }
    )
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @Operation(
            summary = "Изменение сущестующего пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "пользователь успешно изменен",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "неправильное заполнение полей пользователя"
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        User changeUser = userService.updateUser(id, user);
        if (changeUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(changeUser);
    }

    @Operation(
            summary = "Удаление пользователя из базы по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно удален"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь с таким id не найден"
                    )
            }
    )
    @DeleteMapping("{id}")
    public void removeUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
