package pro.sky.animalsheltertelegrambot.model;

import jakarta.persistence.*;
import lombok.*;

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

@Entity
@Data
@NoArgsConstructor
@Table(name = "shelters")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String contacts;
    private String shelterType;
    private String securityContacts;
    private String workingHours;

    /**
     * животные живущие в определенном приюте
     * !! проверить правильность и установить связь !!
     * private Set<Pet> pets
     */

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
