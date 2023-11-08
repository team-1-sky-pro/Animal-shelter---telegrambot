package pro.sky.animalsheltertelegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Pet;

/**
 * PetService предоставляет базовые CRUD операции
 */
@Service
public interface PetService {
    void addPet(Pet pet);
    void updatePet(Long id, Pet pet);
    Pet getPet(Long id);
    void deletePet(Long id);
    Pet findOrThrow(Long id);
    boolean existsById(Long id);

    public void processAnimalCallback(Long chatId, String callbackData);

    public void sendAnimalDetails(Long chatId, Long animalId);
    Pet save(Pet pet);
}
