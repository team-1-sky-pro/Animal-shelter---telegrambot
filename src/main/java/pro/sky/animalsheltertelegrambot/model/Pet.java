package pro.sky.animalsheltertelegrambot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "[a-zA-Zа-яА-Я]+", message = "должно содержать только русские или английский буквы")
    private String petName;

    @PastOrPresent
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private Photo photo;

    /**
     * Статус, который присвоен питомцу
     */
    private boolean isAdopted;
}
