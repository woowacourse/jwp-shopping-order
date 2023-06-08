package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductPriceTest {

    @ParameterizedTest(name = "1~10,000,000 사이의 금액이 들어오면 정상적으로 생성된다.")
    @ValueSource(ints = {1, 10_000_000})
    void create(final int price) {
        assertDoesNotThrow(() -> ProductPrice.create(price));
    }

    @ParameterizedTest(name = "1 미만, 10,000,000 초과의 미만이 들어오면 예외가 발생한다.")
    @ValueSource(ints = {0, 10_000_001})
    void create_fail(final int price) {
        assertThatThrownBy(() -> ProductPrice.create(price))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.PRODUCT_PRICE_RANGE);
    }
}
