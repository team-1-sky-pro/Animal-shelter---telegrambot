package pro.sky.animalsheltertelegrambot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.ShelterNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Shelter;
import pro.sky.animalsheltertelegrambot.repository.ShelterRepository;
import pro.sky.animalsheltertelegrambot.service.ShelterService;

import java.util.Collection;
import java.util.Optional;

import static pro.sky.animalsheltertelegrambot.utils.MethodNameRetriever.getMethodName;

/**
 * реализация сервиса для Приютов
 * базовые CRUD операции
 * + показ всех приютов
 * @ author SyutinS
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterServiceImpl implements ShelterService {

    private final ShelterRepository shelterRepository;

    @Override
    public Shelter addShelter(Shelter shelter) {
        log.info("Was invoked method " + getMethodName());
        return shelterRepository.save(shelter);
    }

    @Override
    public Optional<Shelter> getShelter(Long id) {
        log.info("Was invoked method " + getMethodName());
        findOrThrow(id);
        return shelterRepository.findById(id);
    }

    @Override
    public Shelter updateShelter(Long id, Shelter shelter) {
        log.info("Was invoked method " + getMethodName());
        findOrThrow(id);
        return shelterRepository.save(shelter);
    }

    @Override
    public void deleteShelter(Long id) {
        log.info("Was invoked method " + getMethodName());
        findOrThrow(id);
        shelterRepository.deleteById(id);
    }

    @Override
    public Collection<Shelter> getAllShelters() {
        log.info("Was invoked method " + getMethodName());
        return shelterRepository.findAll();
    }

    private void findOrThrow(Long id) {
        log.info("Was invoked method " + getMethodName());
        Shelter shelter = shelterRepository.findById(id).orElse(null);
        if (shelter == null) {
            log.error("Shelter with id = {} not exist", id);
            throw new ShelterNotFoundException(" Shelter with id not found");
        }
    }

    //метод для получения информации о приюте из бд
    @Override
    public SendMessage displayDogShelterContacts(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId,
                shelterRepository.findDogShelterContactsByShelterType() +
                        "\nБолее подробная информация о приюте в файле:" );
        return sendMessage;
    }

    //    @Override
//    public SendMessage displayCatShelterContacts(Long chatId) {
//        SendMessage sendMessage = new SendMessage(chatId,
//                shelterRepository.findCatShelterContactsByShelterType()
//        + "\nПодробная информация в файле:");
//        return sendMessage;
//    }

    //метод для получения информации о контактах охраны из бд
    @Override
    public SendMessage displayDogShelterSecurityContacts(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId,
                shelterRepository.findDogShelterSecurityContactsByShelterType() +
                        "\nБолее подробная информация в файле:");
        return sendMessage;
    }

//    @Override
//    public SendMessage displayCatShelterSecurityContacts(Long chatId) {
//        SendMessage sendMessage = new SendMessage(chatId,
//                shelterRepository.findCatShelterSecurityContactsByShelterType()
//                + "\nПодробная информация в файле:");
//        return sendMessage;
//    }

    //метод для получения информации о часах работы, схемы проезда и адреса из бд
    @Override
    public SendMessage displayDogShelterWorkingHours(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId,
                shelterRepository.findDogShelterWorkingHoursByShelterType() +
                        "\nБолее подробная информация в файле:");
        return sendMessage;
    }

//    @Override
//    public SendMessage displayCatShelterWorkingHours(Long chatId) {
//        SendMessage sendMessage = new SendMessage(chatId,
//                shelterRepository.findCatShelterWorkingHoursByShelterType()
//                        + "\nПодробная информация в файле:");
//        return sendMessage;
//    }

}
