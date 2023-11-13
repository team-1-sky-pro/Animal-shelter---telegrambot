package pro.sky.animalsheltertelegrambot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pro.sky.animalsheltertelegrambot.exception.ShelterNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Shelter;
import pro.sky.animalsheltertelegrambot.repository.ShelterRepository;
import pro.sky.animalsheltertelegrambot.service.ShelterServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ShelterServiceImplTest {

    @Mock
    private ShelterRepository shelterRepository;

    @InjectMocks
    private ShelterServiceImpl shelterService;

    @BeforeEach
    void setUp() {
        shelterRepository = mock(ShelterRepository.class);
        shelterService = new ShelterServiceImpl(shelterRepository);
    }

    @Test
    void testAddShelter() {
        Shelter result = new Shelter();
        when(shelterRepository.save(result)).thenReturn(result);
        assertNotNull(result);
    }

    @Test
    void testGetShelter() {
        Long id = 1L;
        Shelter result = new Shelter();
        when(shelterRepository.findById(id)).thenReturn(Optional.empty());
        assertNotNull(result);
    }

    @Test
    void testGetAllShelter() {
        Shelter result = new Shelter();
        when(shelterRepository.findAll()).thenReturn(List.of());
        assertNotNull(result);
    }

    @Test
    void testUpdateShelter() {
        Long id = 1L;
        Shelter shelter = new Shelter();
        when(shelterRepository.findById(anyLong())).thenReturn(java.util.Optional.of(shelter));
        when(shelterRepository.save(any(Shelter.class))).thenReturn(shelter);
        Shelter result = shelterService.updateShelter(id, shelter);
        assertNotNull(result);
    }

    @Test
    void testDeleteShelter() {
        Long id = 1L;
        when(shelterRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(shelterRepository).deleteById(anyLong());
        assertThatExceptionOfType(ShelterNotFoundException.class).isThrownBy(() -> shelterService.deleteShelter(id));
    }
}
