package pro.sky.animalsheltertelegrambot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.animalsheltertelegrambot.exception.UserNotFoundException;
import pro.sky.animalsheltertelegrambot.model.User;
import pro.sky.animalsheltertelegrambot.repository.AdoptionRepository;
import pro.sky.animalsheltertelegrambot.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    private AdoptionRepository adoptionRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        User user = new User();
        User saveUser = new User();
        when(userRepository.save(user)).thenReturn(saveUser);
        assertNotNull(user);

        User result = userService.addUser(user);

        assertEquals(saveUser, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUser() {
        Long id = 123L;
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(2)).findById(id);
    }

    @Test
    void testGetAllUsers() {
        User result = new User();
        when(userRepository.findAll()).thenReturn(List.of());
        assertNotNull(result);
    }

    @Test
    void testHasContactInfo() {
        Long id = 1L;
        User result = new User();
        when(userRepository.existsByIdAndEmailIsNotNullAndPhoneIsNotNull(id)).thenReturn(true);
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

    @Test
    void testSaveNewUser() {
        Long id = 1L;
        String userName = "Sergey";
        User user = new User();
        userService.saveNewUser(id, userName);
        when(userRepository.save(user)).thenReturn(user);
        assertNotNull(user);
    }

    @Test
    void testCheckIsUserIsNew() {
        Long id = 1L;
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        assertNotNull(user);
    }

//    @Test
//    void testCheckIfUserIsAdopter() {
//    }

//    @Test
//    void testOnApplicationEventUser() {
//    }

//    @Test
//    void testOnAdopterStartEventAdoption() {
//    }

//    @Test
//    void testHandleStart() {
//    }

//    @Test
//    void testSendWelcomeMessage() {
//    }

    @Test
    void testCreateUser() {
        User user = new User();
        User saveUser = new User();
        when(userRepository.save(user)).thenReturn(saveUser);
        assertNotNull(user);

        User result = userService.addUser(user);

        assertEquals(saveUser, result);
        verify(userRepository, times(1)).save(user);
    }
}
