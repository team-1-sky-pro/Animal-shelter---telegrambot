package pro.sky.animalsheltertelegrambot.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Сущность Photo, в соответствующей таблице хранятся фотографии, относящиеся как к питомцам, так и к отчетам.
 * В случае, если сохраняется фотография питомца, которая не относится ни к какому отчету, то в поле report_id записывается null.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;


    /**
     * Путь к файлу, куда сохранилась копия отправленной пользователем фотографии
     */
    private String filePath;

    /**
     * Размер фотографии
     */
    private Long fileSize;

    /**
     * Поле, хранящее тип файла и расширение
     */
    private String mediaType;

    private boolean isInitial;

    /**
     * ID питомца, фотографии которого была передана
     * @see Pet
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    /**
     * ID отчета, к которому была прикреплена фотография питомца.
     * <p>
     * Может быть null.
     * @see Report
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id")
    private Report report;
}
