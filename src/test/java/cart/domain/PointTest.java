package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.InvalidPointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PointTest {

    @Test
    @DisplayName("포인트가 0 보다 작을 경우 예외를 반환한다.")
    void validate() {
        assertThatThrownBy(() -> new Point(-1))
                .isInstanceOf(InvalidPointException.class)
                .hasMessage("남은 포인트는 음수가 될 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("결제 금액에 비례한 적립되는 포인트를 반환한다.")
    @CsvSource(value = {"3000:300", "5000:500"}, delimiter = ':')
    void fromPayment(int payment, int savingPoint) {
        assertThat(Point.fromPayment(payment).getValue()).isEqualTo(savingPoint);
    }
}
