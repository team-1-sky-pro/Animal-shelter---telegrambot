package pro.sky.animalsheltertelegrambot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import pro.sky.animalsheltertelegrambot.model.Adoption;
import pro.sky.animalsheltertelegrambot.service.AdoptionService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AdoptionControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdoptionService adoptionService;

    @Test
    public void addAdoption_Success() throws Exception {
        Adoption adoption = new Adoption(); // предположим, что это ваша сущность
        given(adoptionService.addAdoption(any(Adoption.class))).willReturn(adoption);

        mockMvc.perform(post("/adoptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adoption)))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(adoption)));
    }

    @Test
    public void getAdoption_Success() throws Exception {
        Long id = 1L;
        Adoption adoption = new Adoption();
        given(adoptionService.getAdoption(id)).willReturn(adoption);

        mockMvc.perform(get("/adoptions/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(adoption)));
    }

    @Test
    public void updateAdoption_Success() throws Exception {
        Long id = 1L;
        Adoption adoption = new Adoption();
        given(adoptionService.updateAdoption(eq(id), any(Adoption.class))).willReturn(adoption);

        mockMvc.perform(put("/adoptions/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adoption)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(adoption)));
    }

    @Test
    public void deleteAdoption_Success() throws Exception {
        Long id = 1L;
        willDoNothing().given(adoptionService).deleteAdoption(id);

        mockMvc.perform(delete("/adoptions/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Adoption deleted " + id)));
    }

    @Test
    public void allNoActiveAdoption_Success() throws Exception {
        List<Adoption> adoptions = Arrays.asList(new Adoption(), new Adoption());
        given(adoptionService.allAdoptionIsFalse()).willReturn(adoptions);

        mockMvc.perform(get("/adoptions/noactive"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(adoptions)));
    }
}
