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
    ABOUT_SHELTER_CAT("/about_shelter_cat", "О приюте кошек"),
    ADOPT_ANIMAL("/adopt_animal","Как взять животное"),
    SHELTER_INFO("/shelter_info", "О приюте"),
    CAT_SHELTER_INFO("/cat_shelter_info", "О приюте - Кошки"),
    SECURITY_CONTACTS("/security_contacts", "Контакты охраны"),
    SCHEDULE("/schedule", "Расписание, адрес\nи схема проезда"),
    SAFETY_RECOMMENDATION("/recommendation", "Рекомендации \n по ТБ\nна территории приюта"),
    APPLICATION("/application", "Оставить заявку");

    private final String command;
    private final String text;
}
