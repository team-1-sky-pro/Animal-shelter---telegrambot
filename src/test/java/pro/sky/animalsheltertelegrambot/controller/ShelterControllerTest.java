package pro.sky.animalsheltertelegrambot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pro.sky.animalsheltertelegrambot.model.Shelter;
import pro.sky.animalsheltertelegrambot.service.ShelterService;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ShelterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShelterService shelterService;

    @InjectMocks
    private ShelterController shelterController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shelterController).build();
    }

    @Test
    public void testGetShelterFound() throws Exception {
        Long shelterId = 1L;
        Shelter mockShelter = new Shelter();
        mockShelter.setId(shelterId);

        when(shelterService.getShelter(shelterId)).thenReturn(Optional.of(mockShelter));

        mockMvc.perform(get("/shelters/{id}", shelterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(shelterId));
    }

    @Test
    public void testGetShelterNotFound() throws Exception {
        Long shelterId = 2L;
        when(shelterService.getShelter(shelterId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/shelters/{id}", shelterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllShelters() throws Exception {
        List<Shelter> shelters = Arrays.asList(new Shelter(), new Shelter()); // Создайте список убежищ
        when(shelterService.getAllShelters()).thenReturn(shelters);

        mockMvc.perform(get("/shelters/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(shelters.size())));
    }

    @Test
    public void testCreateShelter() throws Exception {
        Shelter newShelter = new Shelter();

        when(shelterService.addShelter(any(Shelter.class))).thenReturn(newShelter);

        mockMvc.perform(post("/shelters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newShelter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateShelterSuccess() throws Exception {
        Long shelterId = 1L;
        Shelter updatedShelter = new Shelter();
        updatedShelter.setId(shelterId);

        when(shelterService.updateShelter(eq(shelterId), any(Shelter.class))).thenReturn(updatedShelter);

        mockMvc.perform(put("/shelters/{id}", shelterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedShelter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateShelterNotFound() throws Exception {
        Long shelterId = 2L;
        Shelter updatedShelter = new Shelter();
        updatedShelter.setId(shelterId);

        when(shelterService.updateShelter(eq(shelterId), any(Shelter.class))).thenReturn(null);

        mockMvc.perform(put("/shelters/{id}", shelterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedShelter)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRemoveShelter() throws Exception {
        Long shelterId = 1L;

        doNothing().when(shelterService).deleteShelter(shelterId);

        mockMvc.perform(delete("/shelters/{id}", shelterId))
                .andExpect(status().isOk());

        verify(shelterService, times(1)).deleteShelter(shelterId);
    }
}
