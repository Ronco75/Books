package ronco.books.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ronco.books.model.Book;
import ronco.books.model.BookEntity;
import ronco.books.repository.BookRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("When saving a book, it should be converted and saved correctly")
    void save_shouldConvertAndSaveBook() {
        Book bookToSave = Book.builder()
                .isbn("12345")
                .title("Example Title")
                .author("Example Author")
                .build();

        BookEntity savedEntity = BookEntity.builder()
                .isbn("12345")
                .title("Example Title")
                .author("Example Author")
                .build();

        ArgumentCaptor<BookEntity> bookEntityCaptor = ArgumentCaptor.forClass(BookEntity.class);

        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedEntity);

        Book result = bookService.save(bookToSave);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo(bookToSave.getIsbn());
        assertThat(result.getTitle()).isEqualTo(bookToSave.getTitle());
        assertThat(result.getAuthor()).isEqualTo(bookToSave.getAuthor());

        verify(bookRepository).save(bookEntityCaptor.capture());
        BookEntity capturedEntity = bookEntityCaptor.getValue();

        assertThat(capturedEntity.getIsbn()).isEqualTo(bookToSave.getIsbn());
        assertThat(capturedEntity.getTitle()).isEqualTo(bookToSave.getTitle());
        assertThat(capturedEntity.getAuthor()).isEqualTo(bookToSave.getAuthor());
    }

    @Test
    @DisplayName("When searching for an existing book by ID, it should return the matching book")
    void findById_whenBookExists_shouldReturnBook() {
        String isbn = "12345";
        BookEntity foundEntity = BookEntity.builder()
                .isbn(isbn)
                .title("Existing Title")
                .author("Existing Author")
                .build();

        when(bookRepository.findById(isbn)).thenReturn(Optional.of(foundEntity));

        Optional<Book> result = bookService.findById(isbn);

        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo(isbn);
        assertThat(result.get().getTitle()).isEqualTo(foundEntity.getTitle());
        assertThat(result.get().getAuthor()).isEqualTo(foundEntity.getAuthor());

        verify(bookRepository, times(1)).findById(isbn);
    }

    @Test
    @DisplayName("When searching for a non-existent book, it should return an empty Optional")
    void findById_whenBookDoesNotExist_shouldReturnEmpty() {
        String isbn = "nonexistent";
        when(bookRepository.findById(isbn)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.findById(isbn);

        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findById(isbn);
    }

    @Test
    @DisplayName("When requesting all books, it should return a complete and correctly converted list")
    void listBooks_shouldReturnAllBooks() {
        List<BookEntity> bookEntities = Arrays.asList(
                BookEntity.builder().isbn("1").title("Book 1").author("Author 1").build(),
                BookEntity.builder().isbn("2").title("Book 2").author("Author 2").build()
        );

        when(bookRepository.findAll()).thenReturn(bookEntities);

        List<Book> result = bookService.listBooks();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIsbn()).isEqualTo("1");
        assertThat(result.get(0).getTitle()).isEqualTo("Book 1");
        assertThat(result.get(0).getAuthor()).isEqualTo("Author 1");

        assertThat(result.get(1).getIsbn()).isEqualTo("2");
        assertThat(result.get(1).getTitle()).isEqualTo("Book 2");
        assertThat(result.get(1).getAuthor()).isEqualTo("Author 2");

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("When requesting an empty book list, it should return an empty list")
    void listBooks_whenNoBooks_shouldReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> result = bookService.listBooks();

        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Checking if a book exists should query the repository by ID")
    void isBookExist_shouldCheckRepository() {
        Book book = Book.builder().isbn("12345").build();
        when(bookRepository.existsById("12345")).thenReturn(true);

        boolean result = bookService.isBookExist(book);

        assertThat(result).isTrue();
        verify(bookRepository, times(1)).existsById("12345");
    }

    @Test
    @DisplayName("When deleting an existing book, it should be removed from the repository")
    void deleteBookById_whenBookExists_shouldDeleteFromRepository() {
        String isbn = "12345";

        doNothing().when(bookRepository).deleteById(isbn);

        bookService.deleteBookById(isbn);

        verify(bookRepository, times(1)).deleteById(isbn);
    }

    @Test
    @DisplayName("When attempting to delete a non-existent book, it should catch the exception and continue")
    void deleteBookById_whenBookDoesNotExist_shouldHandleException() {
        String isbn = "nonexistent";

        doThrow(new org.springframework.dao.EmptyResultDataAccessException("Book not found", 1))
                .when(bookRepository).deleteById(isbn);

        bookService.deleteBookById(isbn);

        verify(bookRepository, times(1)).deleteById(isbn);
    }
}