package pro.sky.animalsheltertelegrambot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    @Test
    void testGetUserById() throws Exception {
        User user = new User(8L, "Sergey", false);
        mockMvc.perform(get("/users/id/8")//).perform//(get("/user/id/3"))
                        .accept(String.valueOf(status().is(200)))
                        .accept(String.valueOf(jsonPath("$.id").value(8)))
                        .accept(String.valueOf(jsonPath("$.name").value("Sergey"))))
//                .andExpect(jsonPath("$.isVolunteer").value(false));

                .andExpect(status().is(404));
//                .andExpect(jsonPath("$.id").value(8))
//                .andExpect(jsonPath("$.name").value("Sergey"))
//                .andExpect(jsonPath("$.isVolunteer").value(false));
    }

    @Test
    void testUserCreate() throws Exception {
        User user = new User(10L, "Sergey", false);
        mockMvc.perform(post("/users/id/10")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Sergey"))
                .andExpect(jsonPath("$.isVolunteer").value(false));

    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User(10L, "Sergey", false);
        long id = user.getId();
        mockMvc.perform(put("/users/{id}", id, user)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Sergey"))
                .andExpect(jsonPath("$.isVolunteer").value(false));
    }

    @Test
    void testDeleteUser() throws Exception {
        User user = new User(111L, "Sergey", false);
        Long id = user.getId();
        mockMvc.perform(delete("/users/id", id))
                .andExpect(status().is(400));
    }
}
