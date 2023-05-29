package cart.domain.product;

import cart.exception.GlobalException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductPriceTest {

    @ParameterizedTest(name = "상품 가격은 1원 이상, 10,000,000원 이하여야 합니다.")
    @ValueSource(ints = {-1, 0, 10_000_001})
    void createPriceTest1(int price) {
        assertThatThrownBy(() -> new ProductPrice(price))
                .isInstanceOf(GlobalException.class);
    }

    @ParameterizedTest(name = "상품 가격은 1원 이상, 10,000,000원 이하여야 합니다.")
    @ValueSource(ints = {1, 5_000_000, 10_000_000})
    void createPriceTest2(int price) {
        assertDoesNotThrow(() -> new ProductPrice(price));
    }
}
