package ronco.books.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ronco.books.model.BookEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("When saving a book entity, it should be persisted")
    void save_shouldPersistBookEntity() {
        // Arrange
        BookEntity bookEntity = BookEntity.builder()
                .isbn("12345")
                .title("Test Book")
                .author("Test Author")
                .build();

        // Act
        BookEntity savedEntity = bookRepository.save(bookEntity);

        // Assert
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getIsbn()).isEqualTo("12345");
        
        // Verify it's in the database
        BookEntity foundEntity = entityManager.find(BookEntity.class, "12345");
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getTitle()).isEqualTo("Test Book");
        assertThat(foundEntity.getAuthor()).isEqualTo("Test Author");
    }

    @Test
    @DisplayName("When finding a book by ID that exists, it should return the book")
    void findById_whenBookExists_shouldReturnBook() {
        // Arrange
        BookEntity bookEntity = BookEntity.builder()
                .isbn("54321")
                .title("Another Book")
                .author("Another Author")
                .build();
        
        entityManager.persist(bookEntity);
        entityManager.flush();

        // Act
        Optional<BookEntity> foundBook = bookRepository.findById("54321");

        // Assert
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Another Book");
        assertThat(foundBook.get().getAuthor()).isEqualTo("Another Author");
    }

    @Test
    @DisplayName("When finding a book by ID that doesn't exist, it should return empty")
    void findById_whenBookDoesNotExist_shouldReturnEmpty() {
        // Act
        Optional<BookEntity> foundBook = bookRepository.findById("nonexistent");

        // Assert
        assertThat(foundBook).isEmpty();
    }

    @Test
    @DisplayName("When finding all books, it should return all books in the database")
    void findAll_shouldReturnAllBooks() {
        // Arrange
        BookEntity book1 = BookEntity.builder()
                .isbn("111")
                .title("Book 1")
                .author("Author 1")
                .build();
        
        BookEntity book2 = BookEntity.builder()
                .isbn("222")
                .title("Book 2")
                .author("Author 2")
                .build();
        
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.flush();

        // Act
        List<BookEntity> allBooks = bookRepository.findAll();

        // Assert
        assertThat(allBooks).hasSize(2);
        assertThat(allBooks).extracting(BookEntity::getIsbn).containsExactlyInAnyOrder("111", "222");
    }

    @Test
    @DisplayName("When checking if a book exists by ID, it should return true for existing books")
    void existsById_whenBookExists_shouldReturnTrue() {
        // Arrange
        BookEntity bookEntity = BookEntity.builder()
                .isbn("exists")
                .title("Existing Book")
                .author("Existing Author")
                .build();
        
        entityManager.persist(bookEntity);
        entityManager.flush();

        // Act & Assert
        assertThat(bookRepository.existsById("exists")).isTrue();
    }

    @Test
    @DisplayName("When checking if a book exists by ID, it should return false for non-existent books")
    void existsById_whenBookDoesNotExist_shouldReturnFalse() {
        // Act & Assert
        assertThat(bookRepository.existsById("nonexistent")).isFalse();
    }

    @Test
    @DisplayName("When deleting a book by ID, it should be removed from the database")
    void deleteById_shouldRemoveBookFromDatabase() {
        // Arrange
        BookEntity bookEntity = BookEntity.builder()
                .isbn("delete-me")
                .title("Book to Delete")
                .author("Delete Author")
                .build();
        
        entityManager.persist(bookEntity);
        entityManager.flush();
        
        assertThat(entityManager.find(BookEntity.class, "delete-me")).isNotNull();

        // Act
        bookRepository.deleteById("delete-me");
        
        // Assert
        assertThat(entityManager.find(BookEntity.class, "delete-me")).isNull();
    }
}