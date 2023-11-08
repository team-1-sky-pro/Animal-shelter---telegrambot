package pro.sky.animalsheltertelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.PetAlreadyExistsException;
import pro.sky.animalsheltertelegrambot.exception.PetNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.repository.PetRepository;
import pro.sky.animalsheltertelegrambot.service.PetService;

import static pro.sky.animalsheltertelegrambot.utils.MethodNameRetriever.getMethodName;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {
    public final PetRepository petRepository;

    @Override
    public void addPet(Pet pet) {
        logWhenMethodInvoked(getMethodName());
        try {
            petRepository.save(pet);
        } catch (Exception e) {
            log.error("Pet with name '{}' is already in repo", pet.getPetName());
            throw new PetAlreadyExistsException();
        }
    }

    @Override
    public void updatePet(Long id, Pet pet) {
        logWhenMethodInvoked(getMethodName());
        findOrThrow(id);
        addPet(pet);
    }

    @Override
    public Pet getPet(Long id) {
        logWhenMethodInvoked(getMethodName());
        return findOrThrow(id);
    }

    @Override
    public void deletePet(Long id) {
        logWhenMethodInvoked(getMethodName());
        petRepository.delete(findOrThrow(id));
    }

    @Override
    public Pet findOrThrow(Long id) {
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            log.error("Pet with id = {} doesn't exist", id);
            throw new PetNotFoundException();
        }
        return pet;
    }

    @Override
    public boolean existsById(Long id) {
        return petRepository.existsById(id);
    }

    @Override
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    private void logWhenMethodInvoked(String methodName) {
        log.info("Method '{}'", methodName);
    }

}
