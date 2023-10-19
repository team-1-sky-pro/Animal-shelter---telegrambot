package pro.sky.animalsheltertelegrambot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalsheltertelegrambot.model.Animal;
import pro.sky.animalsheltertelegrambot.service.AnimalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("animals")
public class AnimalController {
    private final AnimalService service;

    @PostMapping
    public void addAnimal(@RequestBody Animal animal) {
        service.addAnimal(animal);
    }

    @PutMapping("/{id}")
    public void updateAnimal(@PathVariable Long id, @RequestBody Animal animal) {
        service.updateAnimal(id, animal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimal(@PathVariable Long id) {
        return new ResponseEntity<>(service.getAnimal(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteAnimal(@PathVariable Long id) {
        service.deleteAnimal(id);
    }
}
