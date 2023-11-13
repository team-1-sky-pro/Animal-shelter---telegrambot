package pro.sky.animalsheltertelegrambot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pro.sky.animalsheltertelegrambot.repository.PhotoRepository;
import pro.sky.animalsheltertelegrambot.repository.ShelterRepository;
import pro.sky.animalsheltertelegrambot.service.PhotoService;

import static org.mockito.Mockito.mock;

public class PhotoServiceImplTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    PhotoServiceImpl photoService;

    @BeforeEach
    void setUp() {
        photoRepository = mock(PhotoRepository.class);
    }


}
