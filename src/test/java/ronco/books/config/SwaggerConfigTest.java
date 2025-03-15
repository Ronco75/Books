package ronco.books.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SwaggerConfigTest {

    @Test
    @DisplayName("הקונפיגורציה של Swagger צריכה ליצור אובייקט OpenAPI עם הפרטים הנכונים")
    void api_shouldCreateOpenAPIWithCorrectInfo() {
        // הכנה
        SwaggerConfig swaggerConfig = new SwaggerConfig();

        // הרצה
        OpenAPI openAPI = swaggerConfig.api();

        // בדיקה
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();

        Info info = openAPI.getInfo();
        assertThat(info.getTitle()).isEqualTo("Books API");
        assertThat(info.getVersion()).isEqualTo("1.0");
        assertThat(info.getDescription()).isEqualTo("Books Service API");
    }
}