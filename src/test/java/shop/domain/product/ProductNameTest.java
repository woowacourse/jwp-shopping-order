package shop.domain.product;

import shop.exception.GlobalException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductNameTest {

    @ParameterizedTest(name = "상품 이름은 1글자 이상, 20글자 이하여야 한다.")
    @ValueSource(strings = {"", "이것은 스물 한글자를 만들기 위함입니다"})
    void createProductNameTest1(String name) {
        assertThatThrownBy(() -> new ProductName(name))
                .isInstanceOf(GlobalException.class);
    }

    @ParameterizedTest(name = "상품 이름은 1글자 이상, 20글자 이하여야 한다.")
    @ValueSource(strings = {"1", "이것은 스무글자를 만들기 위함입니다"})
    void createProductNameTest2(String name) {
        assertDoesNotThrow(() -> new ProductName(name));
    }
}
