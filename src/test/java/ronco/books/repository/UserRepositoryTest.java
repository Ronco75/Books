package ronco.books.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ronco.books.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("When saving a user, it should be persisted")
    void save_shouldPersistUser() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .role("ROLE_USER")
                .build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        
        // Verify it's in the database
        User foundUser = entityManager.find(User.class, savedUser.getId());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        assertThat(foundUser.getPassword()).isEqualTo("password123");
        assertThat(foundUser.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("When finding a user by username that exists, it should return the user")
    void findByUsername_whenUserExists_shouldReturnUser() {
        // Arrange
        User user = User.builder()
                .username("existinguser")
                .password("password123")
                .role("ROLE_USER")
                .build();
        
        entityManager.persist(user);
        entityManager.flush();

        // Act
        Optional<User> foundUser = userRepository.findByUsername("existinguser");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("existinguser");
        assertThat(foundUser.get().getPassword()).isEqualTo("password123");
        assertThat(foundUser.get().getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("When finding a user by username that doesn't exist, it should return empty")
    void findByUsername_whenUserDoesNotExist_shouldReturnEmpty() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("When checking if a user exists by username, it should return true for existing users")
    void existsByUsername_whenUserExists_shouldReturnTrue() {
        // Arrange
        User user = User.builder()
                .username("existsuser")
                .password("password123")
                .role("ROLE_USER")
                .build();
        
        entityManager.persist(user);
        entityManager.flush();

        // Act & Assert
        assertThat(userRepository.existsByUsername("existsuser")).isTrue();
    }

    @Test
    @DisplayName("When checking if a user exists by username, it should return false for non-existent users")
    void existsByUsername_whenUserDoesNotExist_shouldReturnFalse() {
        // Act & Assert
        assertThat(userRepository.existsByUsername("nonexistent")).isFalse();
    }
}