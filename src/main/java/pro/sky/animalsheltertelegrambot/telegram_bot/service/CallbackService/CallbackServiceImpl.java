package pro.sky.animalsheltertelegrambot.telegram_bot.service.CallbackService;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.telegram_bot.service.InteractionService.InteractionService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final InteractionService interactionService;

    @Override
    public void processCallback(CallbackQuery callbackQuery) {
        log.info("Метод processCallback получил запрос на обработку callbackQuery");
        interactionService.processCallback(callbackQuery);
    }
}

