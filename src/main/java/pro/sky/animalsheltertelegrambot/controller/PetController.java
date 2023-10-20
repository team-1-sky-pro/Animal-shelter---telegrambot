package pro.sky.animalsheltertelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.service.PetService;

@RestController
@RequiredArgsConstructor
@RequestMapping("pets")
public class PetController {
    private final PetService service;

    @Operation(
            summary = "Добавление нового питомца в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное добавление"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            },
            tags = "Pets"
    )
    @PostMapping
    public void addPet(@RequestBody Pet pet) {
        service.addPet(pet);
    }

    @Operation(
            summary = "Обновление данных существующего в базе питомца",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
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
            },
            tags = "Pets"
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
                            description = "Питомец был найден"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Питомец по переданному id не был найден"
                    )
            },
            tags = "Pets"
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
            },
            tags = "Pets"
    )
    @DeleteMapping("/{id}")
    public void deletePet(@Parameter(description = "id питомца") @PathVariable Long id) {
        service.deletePet(id);
    }
}
