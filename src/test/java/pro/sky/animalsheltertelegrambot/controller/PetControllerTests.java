package pro.sky.animalsheltertelegrambot.controller;


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

import pro.sky.animalsheltertelegrambot.model.Pet;
import pro.sky.animalsheltertelegrambot.service.PetService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.fasterxml.jackson.databind.ObjectMapper;



@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class PetControllerTests {

        private MockMvc mockMvc;

        @Mock
        private PetService service;

        @InjectMocks
        private PetController petController;

        @BeforeEach
        public void setUp() {
            mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
        }

    @Test
    public void testAddPetSuccess() throws Exception {
        Pet newPet = new Pet();

        doNothing().when(service).addPet(any(Pet.class));

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newPet)))
                .andExpect(status().isOk());

        verify(service, times(1)).addPet(any(Pet.class));
    }


    @Test
    public void testUpdatePetSuccess() throws Exception {
        Long petId = 1L;
        Pet petToUpdate = new Pet();

        doNothing().when(service).updatePet(eq(petId), any(Pet.class));

        mockMvc.perform(put("/pets/{id}", petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(petToUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPet() throws Exception {
        Long petId = 1L;
        Pet pet = new Pet();
        pet.setId(petId);

        when(service.getPet(petId)).thenReturn(pet);

        mockMvc.perform(get("/pets/{id}", petId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(petId));
    }

    @Test
    public void testDeletePet() throws Exception {
        Long petId = 1L;

        doNothing().when(service).deletePet(petId);

        mockMvc.perform(delete("/pets/{id}", petId))
                .andExpect(status().isOk());

        verify(service, times(1)).deletePet(petId);
    }

}
