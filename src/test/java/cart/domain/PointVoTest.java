package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("포인트 값객체 관련 테스트")
class PointVoTest {

    @DisplayName("포인트 값객체를 생성한다.")
    @ValueSource(ints = {1, 2, 1000, 10000})
    @ParameterizedTest
    void createPoint(int value) {
        //when
        PointVO pointVo = new PointVO(value);

        // then
        assertEquals(value, pointVo.getValue());
    }

    @DisplayName("포인트 값객체를 음수로 생성할 수 없다.")
    @ValueSource(ints = {-1, -1000, -100000})
    @ParameterizedTest
    void doseNotCreateNegativePoint(int value) {
        // when, then
        assertThatThrownBy(() -> new PointVO(value))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("포인트는 음수가 될 수 없습니다.");

    }
}
