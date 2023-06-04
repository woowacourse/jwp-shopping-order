package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Test
    void 상품이_정상적으로_생성된다() {
        Product product = new Product(1L, "젤리", 1000, "http://image.com/image.png");

        assertAll(
                () -> assertThat(product.getId()).isEqualTo(1L),
                () -> assertThat(product.getName()).isEqualTo("젤리"),
                () -> assertThat(product.getPrice()).isEqualTo(1000),
                () -> assertThat(product.getImageUrl()).isEqualTo("http://image.com/image.png")
        );
    }
}
