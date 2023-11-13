package pro.sky.animalsheltertelegrambot.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.repository.AdoptionRepository;
import pro.sky.animalsheltertelegrambot.service.AdoptionServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class AdoptionServiceImplTest {

    @Mock
    private AdoptionRepository adoptionRepository;

    @InjectMocks
    private AdoptionServiceImpl adoptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAdoption() {
        Adoption adoption = new Adoption();
        when(adoptionRepository.save(any(Adoption.class))).thenReturn(adoption);

        Adoption result = adoptionService.addAdoption(adoption);
        assertNotNull(result);
    }

    @Test
    void testAddAdoptionNull() {
        Adoption result = adoptionService.addAdoption(null);
        assertNull(result);
    }

    @Test
    void testGetAdoption() {
        Long id = 111L;
        Adoption adoption = new Adoption();
        when(adoptionRepository.findById(anyLong())).thenReturn(java.util.Optional.of(adoption));

        Adoption result = adoptionService.getAdoption(id);
        assertNotNull(result);
    }

    @Test
    void testGetAdoptionNull() {
        assertThrows(IllegalArgumentException.class, () -> adoptionService.getAdoption(null));
    }

    @Test
    void testUpdateAdoption() {
        Long id = 111L;
        Adoption adoption = new Adoption();
        when(adoptionRepository.findById(anyLong())).thenReturn(java.util.Optional.of(adoption));
        when(adoptionRepository.save(any(Adoption.class))).thenReturn(adoption);

        Adoption result = adoptionService.updateAdoption(id, adoption);
        assertNotNull(result);
    }

    @Test
    void testDeleteAdoption() {
        Long id = 111L;
        when(adoptionRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(adoptionRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> adoptionService.deleteAdoption(id));
    }

//    @Test
//    void startAdoptionProcess() {
//    }
//
//    @Test
//    void requestContactInfo() {
//    }
//
//    @Test
//    void processContactInfo() {
//    }
//
//    @Test
//    void saveUserContactInfo() {
//    }
//
//    @Test
//    void offerAnimalsToAdopt() {
//    }
//
//    @Test
//    void handleAnimalAdoption() {
//    }
//
//    @Test
//    boolean isAdoptionCallback() {
//        return false;
//    }
//
//    @Test
//    void handleAdoptionCallback() {
//    }
//
//    @Test
//    void processAdoptionApplication() {
//    }
}
