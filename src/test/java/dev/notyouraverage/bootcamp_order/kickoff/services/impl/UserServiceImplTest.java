package dev.notyouraverage.bootcamp_order.kickoff.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import dev.notyouraverage.base.exceptions.RestException;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.UserDTO;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.CreateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.UpdateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.models.postgres.User;
import dev.notyouraverage.bootcamp_order.kickoff.repositories.UserRepository;
import dev.notyouraverage.bootcamp_order.kickoff.transformers.UserTransformer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserTransformer userTransformer;

    private UserServiceImpl userService;

    private User testUser;

    private UserDTO testUserDTO;

    private CreateUserRequest createUserRequest;

    private UpdateUserRequest updateUserRequest;

    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        userTransformer = new UserTransformer();
        userService = new UserServiceImpl(userRepository, userTransformer);

        testUser = User.builder()
                .id(testUserId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        testUserDTO = UserDTO.builder()
                .id(testUserId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        createUserRequest = CreateUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        updateUserRequest = UpdateUserRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();
    }

    @Test
    void createUser_ShouldReturnUserDTO() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserDTO result = userService.createUser(createUserRequest);

        // Then
        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUserDTO() {
        // Given
        String userId = testUserId.toString();
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // When
        UserDTO result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());

        verify(userRepository).findById(testUserId);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowRestException() {
        // Given
        String userId = testUserId.toString();
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        RestException exception = assertThrows(
                RestException.class,
                () -> userService.getUserById(userId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertNotNull(exception.getErrorResponses());

        verify(userRepository).findById(testUserId);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDTOs() {
        // Given
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        List<User> users = Arrays.asList(testUser, user2);

        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDTO> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUserId, result.get(0).getId());
        assertEquals("John", result.get(0).getFirstName());

        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {
        // Given
        List<User> emptyUsers = Arrays.asList();

        when(userRepository.findAll()).thenReturn(emptyUsers);

        // When
        List<UserDTO> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository).findAll();
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUserDTO() {
        // Given
        String userId = testUserId.toString();
        User updatedUser = User.builder()
                .id(testUserId)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDTO result = userService.updateUser(userId, updateUserRequest);

        // Then
        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("jane.smith@example.com", result.getEmail());

        verify(userRepository).findById(testUserId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowRestException() {
        // Given
        String userId = testUserId.toString();
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        RestException exception = assertThrows(
                RestException.class,
                () -> userService.updateUser(userId, updateUserRequest)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertNotNull(exception.getErrorResponses());

        verify(userRepository).findById(testUserId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Given
        String userId = testUserId.toString();
        when(userRepository.existsById(testUserId)).thenReturn(true);

        // When
        assertDoesNotThrow(() -> userService.deleteUser(userId));

        // Then
        verify(userRepository).existsById(testUserId);
        verify(userRepository).deleteById(testUserId);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowRestException() {
        // Given
        String userId = testUserId.toString();
        when(userRepository.existsById(testUserId)).thenReturn(false);

        // When & Then
        RestException exception = assertThrows(
                RestException.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertNotNull(exception.getErrorResponses());

        verify(userRepository).existsById(testUserId);
        verify(userRepository, never()).deleteById(any());
    }
}
