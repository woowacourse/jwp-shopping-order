package cart.domain.product.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductPriceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("가격이 양수가 아니면 예외가 발생한다.")
    void throws_when_price_not_positive(int price) {
        // when, then
        assertThatThrownBy(() -> new ProductPrice(price))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양수여야합니다.");
    }
}
