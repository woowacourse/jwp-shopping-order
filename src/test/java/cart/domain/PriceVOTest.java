package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("가격 값객체 관련 테스트")
class PriceVOTest {

    @DisplayName("가격 값객체를 생성한다.")
    @ValueSource(ints = {1, 2, 1000, 10000})
    @ParameterizedTest
    void createPrice(int value) {
        //when
        PriceVO priceVO = new PriceVO(value);

        // then
        assertEquals(value, priceVO.getPrice());
    }

    @DisplayName("가격 값객체를 음수로 생성할 수 없다.")
    @ValueSource(ints = {-1, -1000, -100000})
    @ParameterizedTest
    void doseNotCreateNegativePrice(int value) {
        // when, then
        assertThatThrownBy(() -> new PriceVO(value))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("가격은 음수가 될 수 없습니다.");
    }
}
