package cart.domain.product;

import cart.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.exception.ErrorCode.INVALID_PRODUCT_NAME_LENGTH;
import static cart.exception.ErrorCode.INVALID_PRODUCT_PRICE_SIZE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductTest {

    @DisplayName("Product가 정상적으로 생성된다.")
    @Test
    void product() {
        // when & then
        assertDoesNotThrow(() -> new Product("치킨", 1000, "http://example.com/chicken.jpg"));
    }

    @DisplayName("Product name이 1글자 미만 20글자 초과 시 INVALID_PRODUCT_NAME_LENGTH 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "상품이름은20글자가넘어가면예외가발생합니다."})
    void product_InvalidNameLength(String invalidName) {
        // when & then
        assertThatThrownBy(() -> new Product(invalidName, 1000, "http://example.com/chicken.jpg"))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_PRODUCT_NAME_LENGTH);
    }

    @DisplayName("Product price가 1원 미만 10_000_000원 초과 시 INVALID_PRODUCT_PRICE_SIZE 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 10_000_001})
    void product_InvalidPriceSize(int invalidPrice) {
        // when & then
        assertThatThrownBy(() -> new Product("치킨", invalidPrice, "http://example.com/chicken.jpg"))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_PRODUCT_PRICE_SIZE);
    }
}
