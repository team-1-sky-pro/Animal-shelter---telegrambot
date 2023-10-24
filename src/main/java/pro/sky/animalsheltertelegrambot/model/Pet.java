package pro.sky.animalsheltertelegrambot.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Pet сущность
 * @author Rnd-mi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "pets")
public class Pet {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
