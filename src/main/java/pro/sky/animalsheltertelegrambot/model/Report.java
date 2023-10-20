package pro.sky.animalsheltertelegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Report сущность
 * @author Rnd-mi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Report {

    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Дата и время, когда был отправлен отчет
     */
    private Date dateTime;

    private String text;

    /**
     * Внешний ключ: ID питомца, к которому относится данный отчет
     * @see Pet
     */
    private Long petId;
}