package ronco.books.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ronco.books.model.User;
import ronco.books.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("When loading an existing user by username, it should return the user")
    void loadUserByUsername_whenUserExists_shouldReturnUser() {
        // Arrange
        String username = "testuser";
        User user = User.builder()
                .username(username)
                .password("encoded_password")
                .role("ROLE_USER")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userService.loadUserByUsername(username);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getPassword()).isEqualTo("encoded_password");
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("When loading a non-existent user, it should throw UsernameNotFoundException")
    void loadUserByUsername_whenUserDoesNotExist_shouldThrowException() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with username: " + username);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("When saving a user, it should encode the password and save to repository")
    void save_shouldEncodePasswordAndSaveUser() {
        // Arrange
        User userToSave = User.builder()
                .username("newuser")
                .password("plaintext_password")
                .role("ROLE_USER")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username("newuser")
                .password("encoded_password")
                .role("ROLE_USER")
                .build();

        when(passwordEncoder.encode("plaintext_password")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Act
        User result = userService.save(userToSave);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("newuser");
        assertThat(result.getPassword()).isEqualTo("encoded_password");
        assertThat(result.getRole()).isEqualTo("ROLE_USER");

        verify(passwordEncoder, times(1)).encode("plaintext_password");
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getUsername()).isEqualTo("newuser");
        assertThat(capturedUser.getPassword()).isEqualTo("encoded_password");
        assertThat(capturedUser.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("When checking if a username exists, it should query the repository")
    void existsByUsername_shouldCheckRepository() {
        // Arrange
        String username = "existinguser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = userService.existsByUsername(username);

        // Assert
        assertThat(result).isTrue();
        verify(userRepository, times(1)).existsByUsername(username);
    }
}