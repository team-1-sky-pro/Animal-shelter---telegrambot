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
import pro.sky.animalsheltertelegrambot.model.Shelter;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.service.ShelterService;

import java.util.Collection;
import java.util.Optional;

/**
 * Контроллер Приютов в ручном режиме
 * стандатрные CRUD операции:
 * @ createShelter - добавление
 * @ getShelter - просмотр
 * @ updateShelter - изменение
 * @ removeShelter - удаление
 * +
 * @ getAllShelters - просмотр всех приютов
 * @ author SyutinS
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/shelters")
@Tag(name = "Приюты для питомцев", description = "Возможные операции (Просмотр, Изменение, Удаление)")
public class ShelterController {
    private final ShelterService shelterService;

    @Operation(
            summary = "Получение приюта для питомцев по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = " найден приют по id ",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = " приют с таким id не найден "
                    )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<Optional<Shelter>> getShelter(@PathVariable Long id) {
        Optional<Shelter> existShelter = shelterService.getShelter(id);
        if (existShelter.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existShelter);
    }

    @Operation(
            summary = "Получение списка всех приютов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = " получены все приюты ",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class))
                            }
                    )
            }
    )
    @GetMapping("/all")
    public Collection<Shelter> getAllShelters() {
        return shelterService.getAllShelters();
    }

    @Operation(
            summary = "Добавление нового Приюта в базу",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "приют успешно добавлен",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "неправильное заполнение полей приюта"
                    )
            }
    )
    @PostMapping
    public Shelter createShelter(@RequestBody Shelter shelter) {
        return shelterService.addShelter(shelter);
    }

    @Operation(
            summary = "Изменение данных Приюта в базе",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "приют успешно изменен",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "неправильное заполнение полей приюта"
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<Shelter> updateShelter(@PathVariable Long id,
                                                 @RequestBody Shelter shelter) {
        Shelter changeShelter = shelterService.updateShelter(id, shelter);
        if (changeShelter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(changeShelter);
    }

    @Operation(
            summary = "Удаление Приюта из базы по id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "приют успешно удален",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Приют с таким id не найден"
                    )
            }
    )
    @DeleteMapping("{id}")
    public void removeShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
    }
}