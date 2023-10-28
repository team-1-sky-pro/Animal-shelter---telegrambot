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
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id",nullable = false)
    @JoinColumn(name = "adoptions", referencedColumnName = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String name;
    @Column(name = "phone", unique = true)
    private String phone;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "is_volunteer", nullable = false)
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

    public User(Long id, String name, boolean isVolunteer) {
        this.id = id;
        this.name = name;
        this.isVolunteer = isVolunteer;
    }
}
