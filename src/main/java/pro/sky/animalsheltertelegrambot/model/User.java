package pro.sky.animalsheltertelegrambot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * User - Все зарегестрированные пользователи в том чесле и волонтеры
 * id - id
 * name - имя
 * phone - контактные телефон
 * email - адрес электронной почты
 * volunteer - проверка является User волонтером или нет (по умолчанию - Нет)
 * @author SyutinS
 */

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String phone;
    private String email;
    private boolean isVolunteer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
