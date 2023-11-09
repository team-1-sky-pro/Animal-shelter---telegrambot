package pro.sky.animalsheltertelegrambot.telegram_bot.button_types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Button {

    REPORT("/report", "Отправить отчет о питомце"),
    ANOTHER_PET("/another_pet", "Взять еще одного питомца"),
    VOLUNTEER("/volunteer", "Помощь волонтера"),
    ABOUT_SHELTER("/about_shelter", "О приюте собак"),
    ABOUT_SHELTER_CAT("/about_shelter_cat", "О приюте кошек"),
    ADOPT_ANIMAL_CAT("/adopt_animal_cat","Как взять животное"),
    ADOPT_ANIMAL_DOG("/adopt_animal_dog","Как взять животное"),
    SHELTER_INFO("/shelter_info", "Подробнее о приюте"),
    CAT_SHELTER_INFO("/cat_shelter_info", "О приюте - Кошки"),
    SECURITY_CONTACTS("/security_contacts", "Контакты охраны"),
    SCHEDULE("/schedule", "Расписание, адрес и тд."),
    SAFETY_RECOMMENDATION("/recommendation", "Безопасность в приюте."),
    APPLICATION_CAT("/application_cat", "Оставить заявку"),
    APPLICATION_DOG("/application_dog", "Оставить заявку");

    private final String command;
    private final String text;
}
