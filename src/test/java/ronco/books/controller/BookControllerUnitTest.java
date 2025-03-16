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
@WebMvcTest(BookController.class)
public class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Captor
    private ArgumentCaptor<Book> bookCaptor;

    @Test
    @DisplayName("GET /books/{isbn} - When book exists, should return the book and 200 OK")
    void getBook_whenBookExists_shouldReturnBook() throws Exception {
        // Arrange
        Book book = Book.builder()
                .isbn("12345")
                .title("Book Title")
                .author("Author Name")
                .build();

        when(bookService.findById("12345")).thenReturn(Optional.of(book));

        // Act and Assert
        mockMvc.perform(get("/books/12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn", is("12345")))
                .andExpect(jsonPath("$.title", is("Book Title")))
                .andExpect(jsonPath("$.author", is("Author Name")));

        verify(bookService, times(1)).findById("12345");
    }

    @Test
    @DisplayName("GET /books/{isbn} - When book does not exist, should return 404 Not Found")
    void getBook_whenBookDoesNotExist_shouldReturnNotFound() throws Exception {
        when(bookService.findById("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/nonexistent"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findById("nonexistent");
    }

    @Test
    @DisplayName("GET /books - Should return all books and 200 OK")
    void listBooks_shouldReturnAllBooks() throws Exception {
        when(bookService.listBooks()).thenReturn(Arrays.asList(
                Book.builder().isbn("1").title("Book 1").author("Author 1").build(),
                Book.builder().isbn("2").title("Book 2").author("Author 2").build()
        ));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].isbn", is("1")))
                .andExpect(jsonPath("$[1].isbn", is("2")));

        verify(bookService, times(1)).listBooks();
    }

    @Test
    @DisplayName("GET /books - When no books exist, should return empty list and 200 OK")
    void listBooks_whenNoBooks_shouldReturnEmptyList() throws Exception {
        when(bookService.listBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(bookService, times(1)).listBooks();
    }

    @Test
    @DisplayName("POST /books - Should create a new book and return 201 Created")
    void createBook_shouldReturnCreatedBook() throws Exception {
        Book bookToCreate = Book.builder()
                .isbn("new-isbn")
                .title("New Book")
                .author("New Author")
                .build();

        when(bookService.save(any(Book.class))).thenReturn(bookToCreate);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn", is("new-isbn")))
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.author", is("New Author")));

        verify(bookService).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();

        assertThat(capturedBook.getIsbn()).isEqualTo("new-isbn");
        assertThat(capturedBook.getTitle()).isEqualTo("New Book");
    }

    @Test
    @DisplayName("PUT /books/{isbn} - Updating an existing book should return 200 OK")
    void updateBook_whenBookExists_shouldReturnUpdatedBook() throws Exception {
        Book bookToUpdate = Book.builder()
                .title("Updated Book")
                .author("Updated Author")
                .build();

        Book updatedBook = Book.builder()
                .isbn("update-isbn")
                .title("Updated Book")
                .author("Updated Author")
                .build();

        when(bookService.isBookExist(any(Book.class))).thenReturn(true);
        when(bookService.save(any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/update-isbn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", is("update-isbn")))
                .andExpect(jsonPath("$.title", is("Updated Book")));

        verify(bookService).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();

        assertThat(capturedBook.getIsbn()).isEqualTo("update-isbn");
        assertThat(capturedBook.getTitle()).isEqualTo("Updated Book");
    }

    @Test
    @DisplayName("PUT /books/{isbn} - Creating a new book with PUT should return 201 Created")
    void updateBook_whenBookDoesNotExist_shouldReturnCreatedStatus() throws Exception {
        Book newBook = Book.builder()
                .title("New Book")
                .author("New Author")
                .build();

        Book savedBook = Book.builder()
                .isbn("new-isbn")
                .title("New Book")
                .author("New Author")
                .build();

        when(bookService.isBookExist(any(Book.class))).thenReturn(false);
        when(bookService.save(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(put("/books/new-isbn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn", is("new-isbn")));

        verify(bookService).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();

        assertThat(capturedBook.getIsbn()).isEqualTo("new-isbn");
    }

    @Test
    @DisplayName("DELETE /books/{isbn} - Should delete a book and return 204 No Content")
    void deleteBook_shouldReturnNoContent() throws Exception {
        doNothing().when(bookService).deleteBookById(anyString());

        mockMvc.perform(delete("/books/delete-isbn"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBookById("delete-isbn");
    }
}