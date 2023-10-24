package pro.sky.animalsheltertelegrambot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Pet сущность
 * @author Rnd-mi
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "pets")
public class Pet {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String petName;

    private LocalDate birthday;

    /**
     * Описание питомца, например: внешние особенности, наличие заболеваний
     */
    private String description;

    /**
     * Внешний ключ: ID приюта из таблицы 'shelters', к которому относится данный питомец
     * @see Shelter
     */
    private Long shelterId;

    /**
     * Внешний ключ: ID фото из таблицы 'photos'
     * @see Photo
     */
    private Long photoId;

    /**
     * Статус, который присвоен питомцу
     */
    private boolean isAdopted;
}
