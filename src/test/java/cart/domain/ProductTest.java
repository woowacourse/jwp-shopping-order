package cart.domain;

import cart.domain.product.Product;
import cart.exception.ProductException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @ValueSource(strings = {"", "1"})
    @ParameterizedTest
    void 상품은_이름이_1글자_이하면_예외가_발생한다(String value) {
        assertThatThrownBy(() -> new Product(value, 1000, "http://example.com"))
                .isInstanceOf(ProductException.class);
    }

    @ValueSource(ints = {10, 999, 0})
    @ParameterizedTest
    void 상품은_금액이_1000원_이하면_예외가_발생한다(int price) {
        assertThatThrownBy(() -> new Product("피자", price, "http://example.com"))
                .isInstanceOf(ProductException.class);
    }

    @ValueSource(strings = {"image:", "image.jpg"})
    @ParameterizedTest
    void 상품은_이미지_형식이_다르면_예외가_발생한다(String imageUrl) {
        assertThatThrownBy(() -> new Product("피자", 10000, imageUrl))
                .isInstanceOf(ProductException.class);
    }

    @Test
    void 상품의_정보를_수정한다() {
        Product pizza = new Product("피자", 10000, "http://pizza.com");

        pizza.update("햄버거", BigDecimal.valueOf(20000), "http://hamburger.com");

        assertThat(pizza.getName()).isEqualTo("햄버거");
        assertThat(pizza.getPrice()).isEqualTo(new Money(20000));
        assertThat(pizza.getImageUrl()).isEqualTo("http://hamburger.com");
    }
}
