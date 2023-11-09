package pro.sky.animalsheltertelegrambot.telegram_bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Report;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;
import pro.sky.animalsheltertelegrambot.service.PetService;
import pro.sky.animalsheltertelegrambot.service.ReportService;
import pro.sky.animalsheltertelegrambot.service.ShelterService;

import static pro.sky.animalsheltertelegrambot.telegram_bot.button_types.Button.APPLICATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final InteractionService interactionService;

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        interactionService.processCallback(callbackQuery);
    }
}

