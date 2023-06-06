package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void constructWithNoId() {
        final Product result = new Product("name", 0, "imageUrl");
        assertAll(
                () -> assertThat(result.getId()).isNull(),
                () -> assertThat(result.getName()).isEqualTo("name"),
                () -> assertThat(result.getPrice()).isZero(),
                () -> assertThat(result.getImageUrl()).isEqualTo("imageUrl")
        );
    }
}
