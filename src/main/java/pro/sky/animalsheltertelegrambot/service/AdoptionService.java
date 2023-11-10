package pro.sky.animalsheltertelegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Adoption;

public interface AdoptionService {

    Adoption addAdoption(Adoption adoption);

    Adoption getAdoption(Long id);

    Adoption updateAdoption(Long id, Adoption adoption);

    void deleteAdoption(Long id);

    public void saveUserContactInfo(Long userId, String email, String phoneNumber);

    public void offerAnimalsToAdopt(Long chatId, TelegramBot telegramBot, Long shelterId);

    public void handleAnimalAdoption(Long chatId, Long animalId, TelegramBot telegramBot);

    boolean isAdoptionCallback(String data);
    void handleAdoptionCallback(CallbackQuery callbackQuery, TelegramBot telegramBot);

    public void processContactInfo(Long chatId, String text, TelegramBot telegramBot);

    public void startAdoptionProcess(Long chatId, Long shelterId);
}
