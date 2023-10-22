package pro.sky.animalsheltertelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.service.PetService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Питомцы", description = "Возможные операции с питомцами")
@RequestMapping("pets")
public class PetController {
    private final PetService service;

    @Operation(
            summary = "Добавление нового питомца в базу",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новый питомец"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное добавление"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            }
    )
    @PostMapping
    public void addPet(@RequestBody Pet pet) {
        service.addPet(pet);
    }

    @Operation(
            summary = "Обновление данных существующего в базе питомца",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Питомец с новыми данными"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное добавление"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            }
    )
    @PutMapping("/{id}")
    public void updatePet(@Parameter(description = "id питомца") @PathVariable Long id,
                          @RequestBody Pet pet) {
        service.updatePet(id, pet);
    }

    @Operation(
            summary = "Получение питомца из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Питомец был найден",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Pet.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Питомец по переданному id не был найден"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPet(@Parameter(description = "id питомца") @PathVariable Long id) {
        return new ResponseEntity<>(service.getPet(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление питомца из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Питомец по переданному id не был найден"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void deletePet(@Parameter(description = "id питомца") @PathVariable Long id) {
        service.deletePet(id);
    }
}
