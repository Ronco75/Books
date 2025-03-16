package ronco.books.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookModelTest {

    // Book class tests
    private static final String TEST_ISBN = "test-isbn";
    private static final String TEST_TITLE = "Test Title";
    private static final String TEST_AUTHOR = "Test Author";

    @Test
    @DisplayName("Book model - Building object with Lombok builder should initialize all fields")
    void book_builderShouldInitializeAllFields() {
        Book book = Book.builder()
                .isbn(TEST_ISBN)
                .title(TEST_TITLE)
                .author(TEST_AUTHOR)
                .build();

        assertThat(book.getIsbn()).isEqualTo(TEST_ISBN);
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(book.getAuthor()).isEqualTo(TEST_AUTHOR);
    }

    @Test
    @DisplayName("Book model - Setters and getters should work correctly")
    void book_setterGetterShouldWorkCorrectly() {
        Book book = new Book();

        book.setIsbn("12345");
        book.setTitle("New Title");
        book.setAuthor("New Author");

        assertThat(book.getIsbn()).isEqualTo("12345");
        assertThat(book.getTitle()).isEqualTo("New Title");
        assertThat(book.getAuthor()).isEqualTo("New Author");
    }

    @Test
    @DisplayName("Book model - Constructor with parameters should initialize all fields")
    void book_constructorWithParametersShouldInitializeAllFields() {
        Book book = new Book(TEST_ISBN, TEST_TITLE, TEST_AUTHOR);

        assertThat(book.getIsbn()).isEqualTo(TEST_ISBN);
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(book.getAuthor()).isEqualTo(TEST_AUTHOR);
    }

    @Test
    @DisplayName("Book model - Equals and hashCode methods should work as expected")
    void book_equalsAndHashCodeShouldWorkCorrectly() {
        Book book1 = Book.builder()
                .isbn("same-isbn")
                .title("Title 1")
                .author("Author 1")
                .build();

        Book book2 = Book.builder()
                .isbn("same-isbn")
                .title("Title 1")
                .author("Author 1")
                .build();

        Book book3 = Book.builder()
                .isbn("different-isbn")
                .title("Title 2")
                .author("Author 2")
                .build();

        assertThat(book1).isEqualTo(book2);
        assertThat(book1).isNotEqualTo(book3);
        assertThat(book1.hashCode()).isEqualTo(book2.hashCode());
        assertThat(book1.hashCode()).isNotEqualTo(book3.hashCode());
    }

    @Test
    @DisplayName("Book model - ToString method should contain all object fields")
    void book_toStringShouldContainAllFields() {
        Book book = Book.builder()
                .isbn("12345")
                .title("Book Title")
                .author("Author Name")
                .build();

        String toStringResult = book.toString();

        assertThat(toStringResult).contains("isbn=12345");
        assertThat(toStringResult).contains("title=Book Title");
        assertThat(toStringResult).contains("author=Author Name");
    }

    // BookEntity class tests

    @Test
    @DisplayName("BookEntity model - Building object with Lombok builder should initialize all fields")
    void bookEntity_builderShouldInitializeAllFields() {
        BookEntity bookEntity = BookEntity.builder()
                .isbn(TEST_ISBN)
                .title(TEST_TITLE)
                .author(TEST_AUTHOR)
                .build();

        assertThat(bookEntity.getIsbn()).isEqualTo(TEST_ISBN);
        assertThat(bookEntity.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(bookEntity.getAuthor()).isEqualTo(TEST_AUTHOR);
    }

    @Test
    @DisplayName("BookEntity model - Setters and getters should work correctly")
    void bookEntity_setterGetterShouldWorkCorrectly() {
        BookEntity bookEntity = new BookEntity();

        bookEntity.setIsbn("12345");
        bookEntity.setTitle("New Title");
        bookEntity.setAuthor("New Author");

        assertThat(bookEntity.getIsbn()).isEqualTo("12345");
        assertThat(bookEntity.getTitle()).isEqualTo("New Title");
        assertThat(bookEntity.getAuthor()).isEqualTo("New Author");
    }

    @Test
    @DisplayName("BookEntity model - Constructor with parameters should initialize all fields")
    void bookEntity_constructorWithParametersShouldInitializeAllFields() {
        BookEntity bookEntity = new BookEntity(TEST_ISBN, TEST_TITLE, TEST_AUTHOR);

        assertThat(bookEntity.getIsbn()).isEqualTo(TEST_ISBN);
        assertThat(bookEntity.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(bookEntity.getAuthor()).isEqualTo(TEST_AUTHOR);
    }

    @Test
    @DisplayName("BookEntity model - Equals and hashCode methods should work as expected")
    void bookEntity_equalsAndHashCodeShouldWorkCorrectly() {
        BookEntity entity1 = BookEntity.builder()
                .isbn("same-isbn")
                .title("Title 1")
                .author("Author 1")
                .build();

        BookEntity entity2 = BookEntity.builder()
                .isbn("same-isbn")
                .title("Title 1")
                .author("Author 1")
                .build();

        BookEntity entity3 = BookEntity.builder()
                .isbn("different-isbn")
                .title("Title 2")
                .author("Author 2")
                .build();

        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1).isNotEqualTo(entity3);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
        assertThat(entity1.hashCode()).isNotEqualTo(entity3.hashCode());
    }

    @Test
    @DisplayName("BookEntity model - ToString method should contain all object fields")
    void bookEntity_toStringShouldContainAllFields() {
        BookEntity bookEntity = BookEntity.builder()
                .isbn("12345")
                .title("Book Title")
                .author("Author Name")
                .build();

        String toStringResult = bookEntity.toString();

        assertThat(toStringResult).contains("isbn=12345");
        assertThat(toStringResult).contains("title=Book Title");
        assertThat(toStringResult).contains("author=Author Name");
    }

    @Test
    @DisplayName("BookEntity model - Verify JPA annotations are configured correctly")
    void bookEntity_jpaAnnotationsShouldBeConfiguredCorrectly() {
        assertThat(BookEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();
        assertThat(BookEntity.class.isAnnotationPresent(jakarta.persistence.Table.class)).isTrue();
    }
}