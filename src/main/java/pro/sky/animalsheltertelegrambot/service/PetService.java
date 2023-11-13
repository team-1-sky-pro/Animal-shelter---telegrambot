package pro.sky.animalsheltertelegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.Photo;

/**
 * PetService предоставляет базовые CRUD операции
 */
public interface PetService {
    void addPet(Pet pet);
    void updatePet(Long id, Pet pet);
    Pet getPet(Long id);
    void deletePet(Long id);
    Pet findOrThrow(Long id);
    boolean existsById(Long id);

    public void processAnimalCallback(Long chatId, String callbackData);

    public void sendAnimalDetails(Long chatId, Long animalId);

    public void sendPhoto(Long chatId, Photo photo);
    Pet save(Pet pet);
}
