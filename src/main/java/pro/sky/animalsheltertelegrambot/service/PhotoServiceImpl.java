package pro.sky.animalsheltertelegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.model.Photo;


@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {


    @Override
    public Photo addPhoto(Photo photo) {
        return null;
    }

    @Override
    public Photo getPhoto(Long id) {
        return null;
    }

    @Override
    public Photo updatePhoto(Long id, Photo photo) {
        return null;
    }

    @Override
    public void deletePhoto(Long id) {

    }
}
