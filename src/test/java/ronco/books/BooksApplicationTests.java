package ronco.books;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // פרופיל הרצה לבדיקות
class BooksApplicationTests {

    @Test
    @DisplayName("האפליקציה צריכה לעלות נכון וה-context להיטען בהצלחה")
    void contextLoads() {
        // בדיקה פשוטה שבודקת שהאפליקציה עולה ללא שגיאות
        // אם הבדיקה עוברת, זה אומר שכל הבינים (beans) נטענו בהצלחה
    }

}



