package pro.sky.animalsheltertelegrambot.telegram_bot.button_types;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Button {
    REPORT("/report", "Отправить отчет о питомце"),
    ANOTHER_PET("/another_pet", "Взять еще одного питомца");

    private final String command;
    private final String text;
}
