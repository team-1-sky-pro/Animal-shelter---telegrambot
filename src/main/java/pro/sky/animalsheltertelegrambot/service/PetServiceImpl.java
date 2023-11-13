package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.PetAlreadyExistsException;
import pro.sky.animalsheltertelegrambot.exception.PetNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.Photo;
import pro.sky.animalsheltertelegrambot.repository.PetRepository;
import pro.sky.animalsheltertelegrambot.service.PetService;

import java.io.File;
import java.time.format.DateTimeFormatter;

import static pro.sky.animalsheltertelegrambot.utils.MethodNameRetriever.getMethodName;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final TelegramBot telegramBot;

    @Override
    public void addPet(Pet pet) {
        log.info("Was invoked method " + getMethodName());
        try {
            petRepository.save(pet);
        } catch (Exception e) {
            log.error("Pet with name '{}' is already in repo", pet.getPetName());
            throw new PetAlreadyExistsException();
        }
    }

    @Override
    public void updatePet(Long id, Pet pet) {
        log.info("Was invoked method " + getMethodName());
        findOrThrow(id);
        addPet(pet);
    }

    @Override
    public Pet getPet(Long id) {
        log.info("Was invoked method " + getMethodName());
        return findOrThrow(id);
    }

    @Override
    public void deletePet(Long id) {
        log.info("Was invoked method " + getMethodName());
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

    public void processAnimalCallback(Long chatId, String callbackData) {
        Long animalId = Long.parseLong(callbackData.split("_")[1]);
        sendAnimalDetails(chatId, animalId);
    }

    public void sendAnimalDetails(Long chatId, Long animalId) {
        log.info("Looking for animal with ID: {}", animalId);
        Pet animal = petRepository.findByIdAndFetchPhoto(animalId).orElse(null);

        sendText(chatId, animal);

        if (animal != null && animal.getPhoto() != null) {
            sendPhoto(chatId, animal.getPhoto());
        }
    }

    public void sendText(Long chatId, Pet pet) {
        if (pet != null) {
            String text = "Кличка: " + pet.getPetName() +
                    "\nОписание : " + pet.getDescription() +
                    "\nДень рождения: " + pet.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            SendMessage sendMessage = new SendMessage(chatId, text);
            telegramBot.execute(sendMessage);
        } else {
            SendMessage sendMessage = new SendMessage(chatId, "Информация о питомце не найдена.");
            telegramBot.execute(sendMessage);
        }
    }

    public void sendPhoto(Long chatId, Photo photo) {
        if (photo != null) {
            String filePath = photo.getFilePath();
            File file = new File(filePath);
            SendPhoto sendPhoto = new SendPhoto(chatId, file);
            telegramBot.execute(sendPhoto);
        }
    }
}
