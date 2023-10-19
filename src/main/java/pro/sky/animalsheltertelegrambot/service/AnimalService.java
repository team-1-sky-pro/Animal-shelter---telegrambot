package pro.sky.animalsheltertelegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Animal;

/**
 * AnimalService предоставляет базовые CRUD операции
 */
@Service
public interface AnimalService {
    void addAnimal(Animal animal);
    void updateAnimal(Long id, Animal animal);
    Animal getAnimal(Long id);
    void deleteAnimal(Long id);
}
