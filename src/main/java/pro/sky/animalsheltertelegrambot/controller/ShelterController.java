package pro.sky.animalsheltertelegrambot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Shelter;
import pro.sky.animalsheltertelegrambot.service.ShelterService;

import java.util.Collection;

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
@RequestMapping("/shelter")
public class ShelterController {
    private final ShelterService shelterService;

    @GetMapping("{id}")
    public ResponseEntity<Shelter> getShelter(@PathVariable Long id) {
        Shelter existShelter = shelterService.getShelter(id);
        if (existShelter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existShelter);
    }

    @GetMapping("/all")
    public Collection<Shelter> getAllShelters() {
        return shelterService.getAllShelters();
    }

    @PostMapping
    public Shelter createShelter(@RequestBody Shelter shelter) {
        return shelterService.addShelter(shelter);
    }

    @PutMapping("{id}")
    public ResponseEntity<Shelter> updateShelter(@PathVariable Long id,
                                           @RequestBody Shelter shelter) {
        Shelter changeShelter = shelterService.updateShelter(id, shelter);
        if (changeShelter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(changeShelter);
    }

    @DeleteMapping("{id}")
    public void removeShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
    }
}
