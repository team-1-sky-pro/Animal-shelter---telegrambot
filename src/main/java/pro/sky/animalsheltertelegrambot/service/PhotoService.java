package pro.sky.animalsheltertelegrambot.service;

import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.model.Photo;

public interface PhotoService {

    Photo addPhoto(Photo photo);

    Photo getPhoto(Long id);

    Photo updatePhoto(Long id, Photo photo);

    void deletePhoto(Long id);
}
