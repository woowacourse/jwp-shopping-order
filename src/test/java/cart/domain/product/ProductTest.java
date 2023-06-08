package cart.domain.product;

import cart.exception.CartItemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product("테스트", 10, "이미지 주소");
    }

    @Test
    public void 값이_다르면_예외를_반환한다() {
        Product otherProduct = new Product("Test Product", 20, "image-url");

        assertThatThrownBy(() -> product.checkValue(otherProduct))
                .isInstanceOf(CartItemException.PriceIncorrect.class);
    }

    @Test
    public void 값이_값은지_확인한다() {
        Product otherProduct = new Product("Test Product", 10, "image-url");

        Assertions.assertDoesNotThrow(() -> {
            product.checkValue(otherProduct);
        });
    }

}
