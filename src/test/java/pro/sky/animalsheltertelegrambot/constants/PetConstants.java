package pro.sky.animalsheltertelegrambot.constants;

import pro.sky.animalsheltertelegrambot.model.Pet;

import java.time.LocalDate;

public class PetConstants {
    public static final Long ID = 1L;
    public static final Long ID2 = 100L;
    public static final String NAME = "test";
    public static final String NAME2 = "test2";
    public static final LocalDate BIRTHDAY = LocalDate.now();
    public static final String DESCRIPTION = "description";
    public static final Long SHELTER_ID = 1L;
    public static final Long PHOTO_ID = 1L;
    public static final boolean IS_ADOPTED = false;
    public static final Pet PET = new Pet(ID, NAME, BIRTHDAY, DESCRIPTION, SHELTER_ID, PHOTO_ID, IS_ADOPTED);
}
