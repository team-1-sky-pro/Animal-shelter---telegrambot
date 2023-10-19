package pro.sky.animalsheltertelegrambot.model;

import lombok.Data;

import java.util.Objects;

/**
 * Модель Shelter - приют
 * @ address - адрес приюта
 * @ type - тип животного в приюте (кошки/собаки) /специализация приюта
 * @ Security_contact - контактные данные охраны для оформления пропуска на машину
 * @ working_hours - расписание работы приюта
 * @ safety_guide - рекомендации о технике безопасности на территории приюта
 * @ author SyutinS
 */

@Data
public class Shelter {
    private Long id;
    private String address;
    private String type;
    private String security_contact;
    private String working_hours;
    private String safety_guide;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return id.equals(shelter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
