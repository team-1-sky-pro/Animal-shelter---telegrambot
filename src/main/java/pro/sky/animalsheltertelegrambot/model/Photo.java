package pro.sky.animalsheltertelegrambot.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Photo {

    private final Long id;
    private final Long reportsPhoto_id;
    private final boolean isInitial;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo a = (Photo) o;
        return Objects.equals(id, a.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
