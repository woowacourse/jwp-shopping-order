package cart.domain.product.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductNameTest {

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @DisplayName("상품 이름이 빈 값이면 예외가 발생한다.")
    void throws_when_name_blank(String name) {
        // when, then
        assertThatThrownBy(() -> new ProductName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름을 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"@!@E", "1234", "@3121$"})
    @DisplayName("상품 이름이 한글과 영어가 아니면 예외가 발생한다.")
    void throws_when_wrong_product_name(String name) {
        // when, then
        assertThatThrownBy(() -> new ProductName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름은 한글 또는 영어여야합니다.");
    }
}
