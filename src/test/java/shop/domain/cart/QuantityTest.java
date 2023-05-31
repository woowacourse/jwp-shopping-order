package shop.domain.cart;

import shop.exception.GlobalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest {

    @ParameterizedTest(name = "장바구니에 담을 수 있는 상품 개수는 1 ~ 1,000개이다.")
    @ValueSource(ints = {0, 1001})
    void createPriceTest1(int quantity) {
        assertThatThrownBy(() -> new Quantity(quantity))
                .isInstanceOf(GlobalException.class);
    }

    @ParameterizedTest(name = "장바구니에 담을 수 있는 상품 개수는 1 ~ 1,000개이다.")
    @ValueSource(ints = {1, 1000})
    void createPriceTest2(int quantity) {
        Assertions.assertDoesNotThrow(() -> new Quantity(quantity));
    }
}
