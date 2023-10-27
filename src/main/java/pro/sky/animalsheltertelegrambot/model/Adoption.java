package pro.sky.animalsheltertelegrambot.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

@Data
@AllArgsConstructor
@Entity(name = "adoptions")
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Адрес электронной почты, связанный с данным принятием на усыновление.
     * Должен быть действительным адресом электронной почты и уникальным для каждой записи Adoption.
     */
    private LocalDate adoptionDate;
    private LocalDateTime trialEndDate;
    @Column(name = "is_active")
    private boolean isActive;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adoption a = (Adoption) o;
        return Objects.equals(id, a.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
