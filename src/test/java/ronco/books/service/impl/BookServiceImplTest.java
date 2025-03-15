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
        // יצירת מופע חדש של השירות עם מוק של המאגר לפני כל בדיקה
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("כאשר שומרים ספר, צריך להמיר ולשמור אותו בצורה נכונה")
    void save_shouldConvertAndSaveBook() {
        // הכנה - Arrange
        Book bookToSave = Book.builder()
                .isbn("12345")
                .title("כותרת לדוגמה")
                .author("מחבר לדוגמה")
                .build();

        BookEntity savedEntity = BookEntity.builder()
                .isbn("12345")
                .title("כותרת לדוגמה")
                .author("מחבר לדוגמה")
                .build();

        // מתפיסת הארגומנט שנשלח לשיטת save כדי שנוכל לבדוק אותו
        ArgumentCaptor<BookEntity> bookEntityCaptor = ArgumentCaptor.forClass(BookEntity.class);

        // הגדרת התנהגות המוק
        when(bookRepository.save(any(BookEntity.class))).thenReturn(savedEntity);

        // ביצוע - Act
        Book result = bookService.save(bookToSave);

        // בדיקה - Assert
        // בדיקה שהתוצאה נכונה
        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo(bookToSave.getIsbn());
        assertThat(result.getTitle()).isEqualTo(bookToSave.getTitle());
        assertThat(result.getAuthor()).isEqualTo(bookToSave.getAuthor());

        // בדיקה שהמוק נקרא עם הפרמטרים הנכונים
        verify(bookRepository).save(bookEntityCaptor.capture());
        BookEntity capturedEntity = bookEntityCaptor.getValue();

        assertThat(capturedEntity.getIsbn()).isEqualTo(bookToSave.getIsbn());
        assertThat(capturedEntity.getTitle()).isEqualTo(bookToSave.getTitle());
        assertThat(capturedEntity.getAuthor()).isEqualTo(bookToSave.getAuthor());
    }

    @Test
    @DisplayName("כאשר מחפשים ספר קיים לפי מזהה, צריך להחזיר את הספר המתאים")
    void findById_whenBookExists_shouldReturnBook() {
        // הכנה
        String isbn = "12345";
        BookEntity foundEntity = BookEntity.builder()
                .isbn(isbn)
                .title("כותרת קיימת")
                .author("מחבר קיים")
                .build();

        when(bookRepository.findById(isbn)).thenReturn(Optional.of(foundEntity));

        // ביצוע
        Optional<Book> result = bookService.findById(isbn);

        // בדיקה
        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo(isbn);
        assertThat(result.get().getTitle()).isEqualTo(foundEntity.getTitle());
        assertThat(result.get().getAuthor()).isEqualTo(foundEntity.getAuthor());

        // וידוא שהמוק נקרא בדיוק פעם אחת עם המזהה הנכון
        verify(bookRepository, times(1)).findById(isbn);
    }

    @Test
    @DisplayName("כאשר מחפשים ספר שאינו קיים, צריך להחזיר Optional ריק")
    void findById_whenBookDoesNotExist_shouldReturnEmpty() {
        // הכנה
        String isbn = "nonexistent";
        when(bookRepository.findById(isbn)).thenReturn(Optional.empty());

        // ביצוע
        Optional<Book> result = bookService.findById(isbn);

        // בדיקה
        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findById(isbn);
    }

    @Test
    @DisplayName("כאשר מבקשים את כל הספרים, צריך להחזיר רשימה מלאה ומומרת נכון")
    void listBooks_shouldReturnAllBooks() {
        // הכנה
        List<BookEntity> bookEntities = Arrays.asList(
                BookEntity.builder().isbn("1").title("ספר 1").author("מחבר 1").build(),
                BookEntity.builder().isbn("2").title("ספר 2").author("מחבר 2").build()
        );

        when(bookRepository.findAll()).thenReturn(bookEntities);

        // ביצוע
        List<Book> result = bookService.listBooks();

        // בדיקה
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIsbn()).isEqualTo("1");
        assertThat(result.get(0).getTitle()).isEqualTo("ספר 1");
        assertThat(result.get(0).getAuthor()).isEqualTo("מחבר 1");

        assertThat(result.get(1).getIsbn()).isEqualTo("2");
        assertThat(result.get(1).getTitle()).isEqualTo("ספר 2");
        assertThat(result.get(1).getAuthor()).isEqualTo("מחבר 2");

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("כאשר מבקשים רשימת ספרים ריקה, צריך להחזיר רשימה ריקה")
    void listBooks_whenNoBooks_shouldReturnEmptyList() {
        // הכנה
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        // ביצוע
        List<Book> result = bookService.listBooks();

        // בדיקה
        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("בדיקה אם ספר קיים צריכה לבדוק במאגר לפי מזהה")
    void isBookExist_shouldCheckRepository() {
        // הכנה
        Book book = Book.builder().isbn("12345").build();
        when(bookRepository.existsById("12345")).thenReturn(true);

        // ביצוע
        boolean result = bookService.isBookExist(book);

        // בדיקה
        assertThat(result).isTrue();
        verify(bookRepository, times(1)).existsById("12345");
    }

    @Test
    @DisplayName("כאשר מוחקים ספר קיים, צריך למחוק אותו מהמאגר")
    void deleteBookById_whenBookExists_shouldDeleteFromRepository() {
        // הכנה
        String isbn = "12345";

        // מכיוון ש-deleteById לא מחזירה ערך, אין צורך להגדיר התנהגות החזרה
        doNothing().when(bookRepository).deleteById(isbn);

        // ביצוע
        bookService.deleteBookById(isbn);

        // בדיקה שהמתודה נקראה פעם אחת
        verify(bookRepository, times(1)).deleteById(isbn);
    }

    @Test
    @DisplayName("כאשר מנסים למחוק ספר שאינו קיים, צריך ללכוד את השגיאה ולהמשיך")
    void deleteBookById_whenBookDoesNotExist_shouldHandleException() {
        // הכנה
        String isbn = "nonexistent";

        // הגדרת המוק לזרוק שגיאה כשמנסים למחוק ספר שאינו קיים
        doThrow(new org.springframework.dao.EmptyResultDataAccessException("Book not found", 1))
                .when(bookRepository).deleteById(isbn);

        // ביצוע - כאן אנחנו מצפים שלא תיזרק שגיאה למרות שהמוק זורק שגיאה
        bookService.deleteBookById(isbn);

        // בדיקה שהמתודה נקראה
        verify(bookRepository, times(1)).deleteById(isbn);
    }
}