package pro.sky.animalsheltertelegrambot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.sky.animalsheltertelegrambot.exception.PetAlreadyExistsException;
import pro.sky.animalsheltertelegrambot.exception.PetNotFoundException;
import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.repository.PetRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pro.sky.animalsheltertelegrambot.constants.PetConstants.*;

class PetServiceImplTest {
    PetRepository petRepository;

    PetServiceImpl out;

    @BeforeEach
    void setup() {
        petRepository = mock(PetRepository.class);
        out = new PetServiceImpl(petRepository);
    }

    @Test
    @DisplayName(value = "PetRepository 'save' method should be invoked 1 time, if PetServiceImpl 'addPet' is called")
    void testAddPet() {
        when(petRepository.save(any(Pet.class))).thenReturn(PET);
        out.addPet(PET);
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    @DisplayName(value = "'addPet' should throw exception if pet with such a name already exists in repository")
    void testAddPet_shouldThrowIfSuchNameExists() {
        when(petRepository.save(any(Pet.class))).thenThrow(RuntimeException.class);
        assertThatExceptionOfType(PetAlreadyExistsException.class).isThrownBy(() -> out.addPet(PET));
    }

    @Test
    @DisplayName(value = "'updatePet' should throw exception if there is no such pet with given ID")
    void testUpdatePet() {
        when(petRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(PetNotFoundException.class).isThrownBy(() -> out.updatePet(ID, PET));
    }

    @Test
    @DisplayName(value = "'getPet' should throw exception if there is no such pet with given ID")
    void testGetPet() {
        when(petRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(PetNotFoundException.class).isThrownBy(() -> out.getPet(ID));
    }

    @Test
    @DisplayName(value = "'getPet' should throw exception if there is no such pet with given ID")
    void testDeletePet() {
        when(petRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(PetNotFoundException.class).isThrownBy(() -> out.deletePet(ID));
    }
}
