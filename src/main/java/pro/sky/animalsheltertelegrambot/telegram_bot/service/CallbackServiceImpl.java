package pro.sky.animalsheltertelegrambot.telegram_bot.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.PetService;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.APPLICATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final TelegramBot telegramBot;
    private final AdoptionService adoptionService;
    private final PetService petService;


    public void handleCallback(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        log.info("Received callback data: {}", callbackData);
        Long chatId = callbackQuery.message().chat().id();

        if (callbackData.startsWith("ANIMAL_")) {
            petService.processAnimalCallback(chatId, callbackData);
        } else {
            // Другие обработчики колбэков
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        log.info("Received callback data: {}", callbackData);
        Long chatId = callbackQuery.message().chat().id();
        if (callbackData.equals(APPLICATION.toString())) {
            adoptionService.requestContactInfo(chatId, telegramBot);
        } else if (callbackData.startsWith("ANIMAL_")) {
            Long animalId = Long.parseLong(callbackData.split("_")[1]);
            petService.sendAnimalDetails(chatId, animalId);
        } else {
            petService.commandService.receivedCallbackMessage(callbackQuery);

        }
    }
}
