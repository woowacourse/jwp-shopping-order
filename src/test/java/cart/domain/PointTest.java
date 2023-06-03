package cart.domain;

import cart.exception.OrderServerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @DisplayName("값을 빼내어 새로운 Point 객체로 만들 수 있다.")
    @Test
    void subtract_success() {
        Point point1 = Point.from(1000);
        Point point2 = Point.from(500);

        assertThat(point1.subtract(point2).getValue()).isEqualTo(500);
    }

    @DisplayName("작은 값에 큰 값의 포인트를 뺄 수 없다.")
    @Test
    void subtract_fail() {
        Point point1 = Point.from(1000);
        Point point2 = Point.from(500);

        assertThatThrownBy(() -> point2.subtract(point1))
                .isInstanceOf(OrderServerException.class)
                .hasMessageContaining("포인트는 빼려고하는 값이 더 작아야합니다.");
    }
}
