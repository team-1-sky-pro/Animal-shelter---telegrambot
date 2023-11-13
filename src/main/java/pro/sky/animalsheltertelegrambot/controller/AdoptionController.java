package pro.sky.animalsheltertelegrambot.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.exception.AdoptionNotFoundExceptions;
import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.utils.ErrorUtils;

import java.util.List;

@RestController
@RequestMapping("/adoptions")
@RequiredArgsConstructor
public class AdoptionController {

    private final AdoptionService adoptionService;

    @Operation(
            summary = "Добавление нового усыновления в базу",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Новое усыновление"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Успешное добавление"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности"
                    )
            },
            tags = "Adoptions"
    )
    @PostMapping
    public ResponseEntity<?> addAdoption(@Valid @RequestBody Adoption adoption, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Adoption newAdoption = adoptionService.addAdoption(adoption);
        return new ResponseEntity<>(newAdoption, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Получение усыновления по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновление найдено"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Усыновление не найдено"
                    )
            },
            tags = "Adoptions"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdoption(@PathVariable Long id) {
        Adoption existAdoption = adoptionService.getAdoption(id);
        if (existAdoption == null) {
            return new ResponseEntity<>("Adoption not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(existAdoption, HttpStatus.OK);
    }

    @Operation(
            summary = "Обновление данных усыновления по ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Обновленные данные усыновления"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновление успешно обновлено"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильное, неполное заполнение полей сущности или усыновление не найдено"
                    )
            },
            tags = "Adoptions"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdoption(@Valid @PathVariable Long id, @RequestBody Adoption adoption,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorUtils.errorsList(result), HttpStatus.BAD_REQUEST);
        }
        Adoption updateAdoption = adoptionService.updateAdoption(id, adoption);
        return new ResponseEntity<>(updateAdoption, HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление усыновления по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновление успешно удалено"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Усыновление не найдено"
                    )
            },
            tags = "Adoptions"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return new ResponseEntity<>("Adoption deleted " + id, HttpStatus.OK);
    }

    @Operation(
            summary = "Показывает заявки на усыновление",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Заявки найдены"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Заявки не найдены"
                    )
            },
            tags = "Adoptions"
    )
    @GetMapping("/noactive")
    public ResponseEntity<List<Adoption>> allNoActiveAdoption() {
        List<Adoption> noActiveList = adoptionService.allAdoptionIsFalse();
        if (noActiveList.isEmpty()) {
            throw new AdoptionNotFoundExceptions("Нет активных заявок на усыновление.");
        }
        return new ResponseEntity<>(noActiveList, HttpStatus.OK);
    }


}