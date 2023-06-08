package cart.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductNameTest {

    @ParameterizedTest(name = "1~20글자의 이름이 들어오면 정상적으로 생성된다.")
    @ValueSource(strings = {"떡", "안녕하세요이건20글자를채우기위해씁니다"})
    void create(final String name) {
        assertDoesNotThrow(() -> ProductName.create(name));
    }

    @ParameterizedTest(name = "1글자 미만, 20글자 초과의 이름이 들어오면 예외가 발생한다.")
    @ValueSource(strings = {"", "안녕하세요이건20글자를채우기위해씁니다!"})
    void create_fail(final String name) {
        assertThatThrownBy(() -> ProductName.create(name))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.PRODUCT_NAME_LENGTH);
    }
}
