package pro.sky.animalsheltertelegrambot.telegram_bot.button_types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Button {

    REPORT("/report", "Отправить отчет о питомце"),
    ANOTHER_PET("/another_pet", "Взять еще одного питомца"),
    VOLUNTEER("/volunteer", "Помощь волонтера"),
    ABOUT_SHELTER("/about_shelter", "О приюте"),
    ADOPT_ANIMAL("/adopt_animal","Взять животное"),
    DOG_SHELTER_INFO("/dog_shelter_info", "О приюте"),
    SECURITY_CONTACTS("/security_contacts", "Контакты охраны"),
    SCHEDULE("/schedule", "Расписание, адрес и схема проезда"),
    SAFETY_RECOMMENDATION("/recommendation", "Рекомендации  по ТБна территории приюта"),
    APPLICATION("/application", "Оставить заявку");

    private final String command;
    private final String text;
}
