package pro.sky.animalsheltertelegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pro.sky.animalsheltertelegrambot.animal_status.Status;

import java.time.LocalDate;

/**
 * Pet сущность
 * @author Rnd-mi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pet {

    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private LocalDate birthDate;

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
    private Long photoID;

    /**
     * Статус, который присвоен питомцу
     * @see Status
     */
    private Status status;
}
