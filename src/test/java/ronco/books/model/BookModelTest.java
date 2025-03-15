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
    @DisplayName("מודל Book - בניית אובייקט באמצעות בנאי הלומבוק צריכה לאתחל את כל השדות")
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
    @DisplayName("מודל Book - set/get צריכים לעבוד בצורה תקינה")
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
    @DisplayName("מודל Book - בנאי עם פרמטרים צריך לאתחל את כל השדות")
    void book_constructorWithParametersShouldInitializeAllFields() {
        Book book = new Book(TEST_ISBN, TEST_TITLE, TEST_AUTHOR);

        assertThat(book.getIsbn()).isEqualTo(TEST_ISBN);
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(book.getAuthor()).isEqualTo(TEST_AUTHOR);
    }

    @Test
    @DisplayName("מודל Book - שיטת equals ו-hashCode צריכות לעבוד כצפוי")
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
    @DisplayName("מודל Book - שיטת toString צריכה להכיל את כל שדות האובייקט")
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
    @DisplayName("מודל BookEntity - בניית אובייקט באמצעות בנאי הלומבוק צריכה לאתחל את כל השדות")
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
    @DisplayName("מודל BookEntity - set/get צריכים לעבוד בצורה תקינה")
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
    @DisplayName("מודל BookEntity - בנאי עם פרמטרים צריך לאתחל את כל השדות")
    void bookEntity_constructorWithParametersShouldInitializeAllFields() {
        BookEntity bookEntity = new BookEntity(TEST_ISBN, TEST_TITLE, TEST_AUTHOR);

        assertThat(bookEntity.getIsbn()).isEqualTo(TEST_ISBN);
        assertThat(bookEntity.getTitle()).isEqualTo(TEST_TITLE);
        assertThat(bookEntity.getAuthor()).isEqualTo(TEST_AUTHOR);
    }

    @Test
    @DisplayName("מודל BookEntity - שיטת equals ו-hashCode צריכות לעבוד כצפוי")
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
    @DisplayName("מודל BookEntity - שיטת toString צריכה להכיל את כל שדות האובייקט")
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
    @DisplayName("מודל BookEntity - וידוא שהאנוטציות של JPA מוגדרות נכון")
    void bookEntity_jpaAnnotationsShouldBeConfiguredCorrectly() {

        assertThat(BookEntity.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();

        assertThat(BookEntity.class.isAnnotationPresent(jakarta.persistence.Table.class)).isTrue();
    }
}