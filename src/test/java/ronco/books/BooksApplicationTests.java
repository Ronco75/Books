package ronco.books;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BooksApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the application context loads successfully
    }
}