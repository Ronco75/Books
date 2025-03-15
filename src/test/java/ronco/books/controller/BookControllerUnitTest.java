package ronco.books.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ronco.books.model.Book;
import ronco.books.service.BookService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class) // מגדיר שזוהי בדיקת יחידה לבקר בלבד
public class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc; // מאפשר לדמות בקשות HTTP

    @Autowired
    private ObjectMapper objectMapper; // ממיר אובייקטים ל-JSON ובחזרה

    @MockBean
    private BookService bookService; // מוק לשירות

    @Captor
    private ArgumentCaptor<Book> bookCaptor; // לוכד ארגומנטים לבדיקה

    @Test
    @DisplayName("GET /books/{isbn} - כאשר הספר קיים, צריך להחזיר הספר ו-200 OK")
    void getBook_whenBookExists_shouldReturnBook() throws Exception {
        // הכנה
        Book book = Book.builder()
                .isbn("12345")
                .title("כותרת הספר")
                .author("המחבר")
                .build();

        when(bookService.findById("12345")).thenReturn(Optional.of(book));

        // ביצוע ובדיקה
        mockMvc.perform(get("/books/12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn", is("12345")))
                .andExpect(jsonPath("$.title", is("כותרת הספר")))
                .andExpect(jsonPath("$.author", is("המחבר")));

        // וידוא שהשירות נקרא בדיוק פעם אחת עם המזהה הנכון
        verify(bookService, times(1)).findById("12345");
    }

    @Test
    @DisplayName("GET /books/{isbn} - כאשר הספר אינו קיים, צריך להחזיר 404 Not Found")
    void getBook_whenBookDoesNotExist_shouldReturnNotFound() throws Exception {
        // הכנה
        when(bookService.findById("nonexistent")).thenReturn(Optional.empty());

        // ביצוע ובדיקה
        mockMvc.perform(get("/books/nonexistent"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findById("nonexistent");
    }

    @Test
    @DisplayName("GET /books - צריך להחזיר את כל הספרים ו-200 OK")
    void listBooks_shouldReturnAllBooks() throws Exception {
        // הכנה
        when(bookService.listBooks()).thenReturn(Arrays.asList(
                Book.builder().isbn("1").title("ספר 1").author("מחבר 1").build(),
                Book.builder().isbn("2").title("ספר 2").author("מחבר 2").build()
        ));

        // ביצוע ובדיקה
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].isbn", is("1")))
                .andExpect(jsonPath("$[1].isbn", is("2")));

        verify(bookService, times(1)).listBooks();
    }

    @Test
    @DisplayName("GET /books - כאשר אין ספרים, צריך להחזיר רשימה ריקה ו-200 OK")
    void listBooks_whenNoBooks_shouldReturnEmptyList() throws Exception {
        // הכנה
        when(bookService.listBooks()).thenReturn(Collections.emptyList());

        // ביצוע ובדיקה
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(bookService, times(1)).listBooks();
    }

    @Test
    @DisplayName("POST /books - צריך ליצור ספר חדש ולהחזיר 201 Created")
    void createBook_shouldReturnCreatedBook() throws Exception {
        // הכנה
        Book bookToCreate = Book.builder()
                .isbn("new-isbn")
                .title("ספר חדש")
                .author("מחבר חדש")
                .build();

        when(bookService.save(any(Book.class))).thenReturn(bookToCreate);

        // ביצוע ובדיקה
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn", is("new-isbn")))
                .andExpect(jsonPath("$.title", is("ספר חדש")))
                .andExpect(jsonPath("$.author", is("מחבר חדש")));

        // וידוא שהשירות נקרא עם האובייקט הנכון
        verify(bookService).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();

        assertThat(capturedBook.getIsbn()).isEqualTo("new-isbn");
        assertThat(capturedBook.getTitle()).isEqualTo("ספר חדש");
    }

    @Test
    @DisplayName("PUT /books/{isbn} - עדכון ספר קיים צריך להחזיר 200 OK")
    void updateBook_whenBookExists_shouldReturnUpdatedBook() throws Exception {
        // הכנה
        Book bookToUpdate = Book.builder()
                .title("ספר מעודכן")
                .author("מחבר מעודכן")
                .build();

        Book updatedBook = Book.builder()
                .isbn("update-isbn")
                .title("ספר מעודכן")
                .author("מחבר מעודכן")
                .build();

        when(bookService.isBookExist(any(Book.class))).thenReturn(true);
        when(bookService.save(any(Book.class))).thenReturn(updatedBook);

        // ביצוע ובדיקה
        mockMvc.perform(put("/books/update-isbn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", is("update-isbn")))
                .andExpect(jsonPath("$.title", is("ספר מעודכן")));

        // וידוא שהפונקציות במוק נקראו עם הפרמטרים הנכונים
        verify(bookService).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();

        assertThat(capturedBook.getIsbn()).isEqualTo("update-isbn");
        assertThat(capturedBook.getTitle()).isEqualTo("ספר מעודכן");
    }

    @Test
    @DisplayName("PUT /books/{isbn} - יצירת ספר חדש באמצעות PUT צריכה להחזיר 201 Created")
    void updateBook_whenBookDoesNotExist_shouldReturnCreatedStatus() throws Exception {
        // הכנה
        Book newBook = Book.builder()
                .title("ספר חדש")
                .author("מחבר חדש")
                .build();

        Book savedBook = Book.builder()
                .isbn("new-isbn")
                .title("ספר חדש")
                .author("מחבר חדש")
                .build();

        when(bookService.isBookExist(any(Book.class))).thenReturn(false);
        when(bookService.save(any(Book.class))).thenReturn(savedBook);

        // ביצוע ובדיקה
        mockMvc.perform(put("/books/new-isbn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn", is("new-isbn")));

        // וידוא שמזהה הספר הוגדר נכון בבקשה שנשלחה לשירות
        verify(bookService).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();

        assertThat(capturedBook.getIsbn()).isEqualTo("new-isbn");
    }

    @Test
    @DisplayName("DELETE /books/{isbn} - צריך למחוק ספר ולהחזיר 204 No Content")
    void deleteBook_shouldReturnNoContent() throws Exception {
        // הכנה
        doNothing().when(bookService).deleteBookById(anyString());

        // ביצוע ובדיקה
        mockMvc.perform(delete("/books/delete-isbn"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBookById("delete-isbn");
    }
}