package pro.sky.animalsheltertelegrambot.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
//@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long reportPhotoId;
    private boolean isInitial;

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
