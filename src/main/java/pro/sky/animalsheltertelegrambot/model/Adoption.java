package pro.sky.animalsheltertelegrambot.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Adoption {


    private final Long id;

    /**
     * Адрес электронной почты, связанный с данным принятием на усыновление.
     * Должен быть действительным адресом электронной почты и уникальным для каждой записи Adoption.
     */
    private final String email;
    private final String phone;


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
