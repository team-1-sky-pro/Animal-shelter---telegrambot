package pro.sky.animalsheltertelegrambot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pro.sky.animalsheltertelegrambot.exception.UserNotFoundException;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testAddUser() {
        User result = new User();
        when(userRepository.save(result)).thenReturn(result);
        assertNotNull(result);
    }

    @Test
    void testGetUser() {
        Long id = 1L;
        User result = new User();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertNotNull(result);
    }

    @Test
    void TestGetAllUsers() {
        User result = new User();
        when(userRepository.findAll()).thenReturn(List.of());
        assertNotNull(result);
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.updateUser(id, user);
        assertNotNull(result);
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());
        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userService.deleteUser(id));
    }
}
