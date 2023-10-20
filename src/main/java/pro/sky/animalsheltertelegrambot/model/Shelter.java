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
public class Shelter {

    @Id
    @GeneratedValue
    private Long id;
    private String address;
    private String type;
    private String security_contact;
    private String working_hours;
    private String safety_guide;

    /**
     * животные живущие в определенном приюте
     * !! проверить правильность и установить связь !!
     * private Set<Pet> pets
     */
//    @OneToMany(mappedBy = "shelter")
//    private Set<Pet> pets;

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