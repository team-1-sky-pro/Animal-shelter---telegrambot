package pro.sky.animalsheltertelegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pro.sky.animalsheltertelegrambot.exception.PetAlreadyExistsException;
import pro.sky.animalsheltertelegrambot.exception.PetNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.repository.PetRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PetServiceImplTest {

    @Mock
    PetRepository petRepository;
    TelegramBot telegramBot;

    @InjectMocks
    PetServiceImpl petService;

    private final LocalDate birthday = LocalDate.now();

    @BeforeEach
    void setup() {
        petRepository = mock(PetRepository.class);
        petService = new PetServiceImpl(petRepository, telegramBot);
    }

    @Test
    void testAddPet() {
        Pet pet = new Pet(111L, "Бобик", birthday, "descriptionTest", 111L, null, false);
        Pet savePet = new Pet(111L, "Бобик", birthday, "descriptionTest", 111L, null, false);
        when(petRepository.save(pet)).thenReturn(savePet);
        assertNotNull(pet);

        assertEquals(savePet, pet);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        petService.addPet(pet);
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void testAddPet_shouldThrowIfSuchNameExists() {
        Pet pet = new Pet(111L, "Бобик", birthday, "descriptionTest", 111L, null, false);
        when(petRepository.save(any(Pet.class))).thenThrow(RuntimeException.class);
        assertThatExceptionOfType(PetAlreadyExistsException.class).isThrownBy(() -> petService.addPet(pet));
    }

    @Test
    void testUpdatePet() {
        Long id = 111L;
        Pet pet = new Pet(111L, "Бобик", birthday, "descriptionTest", 111L, null, false);
        when(petRepository.findById(id)).thenReturn(Optional.empty());
        assertThatExceptionOfType(PetNotFoundException.class).isThrownBy(() -> petService.updatePet(id, pet));
    }

    @Test
    void testGetPet() {
        Long id = 111L;
        when(petRepository.findById(id)).thenReturn(Optional.empty());
        assertThatExceptionOfType(PetNotFoundException.class).isThrownBy(() -> petService.getPet(id));
    }

    @Test
    void testDeletePet() {
        Long id = 111L;
        when(petRepository.findById(id)).thenReturn(Optional.empty());
        assertThatExceptionOfType(PetNotFoundException.class).isThrownBy(() -> petService.deletePet(id));
    }

    @Test
    void testFindOrThrow() {
        Long id = 111L;
        when(petRepository.findById(id)).thenThrow(PetNotFoundException.class);
        assertThatExceptionOfType(PetNotFoundException.class).isThrownBy(() -> petService.deletePet(id));
    }

    @Test
    void testExistsById() {
        Long id = 111L;
        Pet pet = new Pet();
        when(petRepository.existsById(id)).thenReturn(false);
        assertNotNull(pet);
    }

    @Test
    void testSave() {
        Pet pet = new Pet(111L, "Бобик", birthday, "descriptionTest", 111L, null, false);
        when(petRepository.save(any(Pet.class))).thenThrow(RuntimeException.class);
        assertThatExceptionOfType(PetAlreadyExistsException.class).isThrownBy(() -> petService.addPet(pet));
    }

//    @Test
//    void testProcessAnimalCallback() {
//    }

    @Test
    void testSendAnimalDetails() {
        Long id = 111L;
        Pet pet = new Pet(111L, "Бобик", birthday, "descriptionTest", 111L, null, false);
        when(petRepository.findByIdAndFetchPhoto(id)).thenReturn(null);
        assertNotNull(pet);
    }

//    @Test
//    void testSendText() {
//    }

//    @Test
//    void testSendPhoto() {
//    }
}
