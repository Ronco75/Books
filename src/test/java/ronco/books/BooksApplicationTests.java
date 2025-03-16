package ronco.books;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BooksApplicationTests {

    @Test
    @DisplayName("Application should start correctly and the context should load successfully")
    void contextLoads() {
    }

}



