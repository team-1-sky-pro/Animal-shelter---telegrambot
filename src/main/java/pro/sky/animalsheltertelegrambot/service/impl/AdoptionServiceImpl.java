package pro.sky.animalsheltertelegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.exception.AdoptionNotFoundExceptions;
import pro.sky.animalsheltertelegrambot.exception.PetNotFoundException;
import pro.sky.animalsheltertelegrambot.exception.UserNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.AdoptionRepository;
import pro.sky.animalsheltertelegrambot.repository.PetRepository;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.PetService;
import pro.sky.animalsheltertelegrambot.service.PhotoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация сервиса по управлению усыновлением.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdoptionServiceImpl implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final TelegramBot telegramBot;
    private final PetService petService;

    private final Map<Long, Long> userShelterPreference = new HashMap<>();

    /**
     * Добавляет новое усыновление.
     *
     * @param adoption Усыновление, которую нужно добавить.
     * @return Добавлено усыновления.
     */
    @Override
    public Adoption addAdoption(Adoption adoption) {
        if (adoption == null) {
            log.error("Attempt to add a null adoption");
            return null;
        }

        log.info("Adding new adoption: {}", adoption);
        return adoptionRepository.save(adoption);
    }

    /**
     * Получает усыновление по его Id.
     *
     * @param id Id усыновления, который требуется получить.
     * @return Найденное усыновление.
     * @throws AdoptionNotFoundExceptions Если усыновление не обнаружено.
     * @throws IllegalArgumentException   Если Id равен null.
     */
    @Override
    public Adoption getAdoption(Long id) {
        if (id == null) {
            log.error("Attempt to get adoption with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }
        log.info("Fetching adoption with ID: {}", id);
        return adoptionRepository.findById(id).orElseThrow(
                () -> new AdoptionNotFoundExceptions("Adoption this Id not found."));
    }

    /**
     * Обновляет существующее усыновление.
     *
     * @param id       Id усыновления, который необходимо обновить.
     * @param adoption Усыновления с обновленными полями.
     * @return Обновленное усыновление.
     * @throws NullPointerException Если данные Id или усыновления равны null.
     */
    @Override
    public Adoption updateAdoption(Long id, Adoption adoption) {
        if (id == null || adoption == null) {
            log.error("Invalid ID or adoption data for update");
            throw new IllegalArgumentException("ID and Adoption cannot be null");
        }
        Adoption updatesAdoption = getAdoption(id);
        if (updatesAdoption == null) {
            log.error("Adoption with ID {} not found", id);
            throw new IllegalArgumentException("Updates Adoption and Adoption cannot be null");
        }
        updatesAdoption.setAdoptionDate(adoption.getAdoptionDate());
        updatesAdoption.setTrialEndDate(adoption.getTrialEndDate());
        updatesAdoption.setActive(adoption.isActive());

        log.info("Updating adoption with ID: {}", id);
        return adoptionRepository.save(updatesAdoption);
    }

    /**
     * Удаляет усыновление по его Id.
     *
     * @param id Id усыновления, которое необходимо удалить.
     * @throws AdoptionNotFoundExceptions Если усыновление не обнаружено.
     * @throws NullPointerException       Если Id равен null.
     */
    @Override
    public void deleteAdoption(Long id) {
        if (id == null) {
            log.error("Attempt to delete adoption with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }

        if (!adoptionRepository.existsById(id)) {
            log.error("Adoption with ID {} not found", id);
            throw new AdoptionNotFoundExceptions("Adoption this Id not found");
        }

        log.info("Deleting adoption with ID: {}", id);
        adoptionRepository.deleteById(id);
    }



    //===================================start Adoption =================================================================

    public void startAdoptionProcess(Long chatId, Long shelterId) {
        userShelterPreference.put(chatId, shelterId);
        SendMessage requestContact = new SendMessage(chatId, "Пожалуйста, введите ваш email и номер телефона через запятую (например, email@example.com, +1234567890)");
        telegramBot.execute(requestContact);
    }


    public void processContactInfo(Long chatId, String text, TelegramBot telegramBot) {
        // Проверяем, соответствует ли введённый текст шаблону email и номера телефона
        if (text.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,},\\s*\\+?\\d{10,15}$")) {
            // Разделяем текст на email и номер телефона
            String[] parts = text.split(",", 2);
            String email = parts[0].trim();
            String phoneNumber = parts[1].trim();

            // Сохраняем данные
            saveUserContactInfo(chatId, email, phoneNumber);

            // Дополнительная логика или сообщение пользователю
            String responseText = "Спасибо! Ваши контактные данные сохранены.";
            SendMessage responseMessage = new SendMessage(chatId, responseText);
            telegramBot.execute(responseMessage);

            Long shelterId = userShelterPreference.get(chatId);
            if (shelterId != null) {
                offerAnimalsToAdopt(chatId, telegramBot, shelterId);
                log.info("Вызов метода offerAnimalsToAdopt в методе processContactInfo" + chatId);
            } else {
                throw new PetNotFoundException();
            }
            // Предложение выбрать животное
        } else {
            // Если текст не соответствует шаблону, просим ввести данные ещё раз
            String responseText = "Пожалуйста, проверьте данные и введите их в правильном формате.";
            log.info("Ввели данные неккоректно, отправили предупреждение");
            SendMessage responseMessage = new SendMessage(chatId, responseText);
            telegramBot.execute(responseMessage);
        }
    }



    @Override
    public void saveUserContactInfo(Long userId, String email, String phoneNumber) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setEmail(email);
            user.setPhone(phoneNumber);
            userRepository.save(user);
            log.info("Saved user contact info for userId: {}", userId);
        } else {
            log.warn("User not found for userId: {}", userId);
        }
    }

    @Override
    public void offerAnimalsToAdopt(Long chatId, TelegramBot telegramBot, Long shelterId) {
        // Получение списка доступных животных для усыновления
        log.info("Вызвали метод offerAnimalsToAdopt для: " + chatId + " ");
        List<Pet> availableAnimals = petRepository.findAllByShelterIdAndIsAdoptedFalse(shelterId);

        // Создание кнопок для каждого доступного животного
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (Pet animal : availableAnimals) {
            InlineKeyboardButton button = new InlineKeyboardButton(animal.getPetName())
                    .callbackData("ANIMAL_" + animal.getId());
            inlineKeyboardMarkup.addRow(button);
        }

        // Отправка сообщения с кнопками
        SendMessage message = new SendMessage(chatId, "Выберите животное для усыновления:")
                .replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(message);
    }


    @Override
    public void handleAnimalAdoption(Long chatId, Long petId, TelegramBot telegramBot) {
        // Проверка существования животного
        Pet animal = petRepository.findById(petId).orElse(null);
        if (animal != null && !animal.isAdopted()) {
            petService.sendAnimalDetails(chatId, animal.getId());
            log.info("User {} started adoption process for animalId: {}", chatId, petId);
        } else {
            // Животное не найдено или уже усыновлено
            SendMessage message = new SendMessage(chatId, "Извините, это животное уже усыновлено или не существует.");
            telegramBot.execute(message);
            log.warn("Adoption attempt for unavailable animalId: {}", petId);
        }
    }

    @Override
    public boolean isAdoptionCallback(String data) {
        return data != null && data.startsWith("ANIMAL_");
    }

    public void handleAdoptionCallback(CallbackQuery callbackQuery, TelegramBot telegramBot) {
        log.info("Вызываем метод handleAdoptionCallback " + callbackQuery);
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();

        log.info("Обрабатываем колбек для чата ID: {}", chatId);

        if (isAdoptionCallback(callbackData)) {
            // Извлекаем ID животного из callbackData и вызываем метод отправки информации
            Long animalId = Long.parseLong(callbackData.split("_")[1]);
            handleAnimalAdoption(chatId, animalId, telegramBot);
        } else {
            log.warn("Received unknown callback data: {}", callbackData);
            SendMessage message = new SendMessage(chatId, "Извините, мы не смогли распознать запрос.");
            telegramBot.execute(message);
        }
    }

}
