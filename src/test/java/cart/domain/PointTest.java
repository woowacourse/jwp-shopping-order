package cart.domain;

import cart.exception.OrderServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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


    @DisplayName("동일한 달에 만료가 된다면 true를 반환하고, 아니라면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2023.03.13:2023.03.30:true", "2023.02.28:2023.02.28:true", "2023.10.13:2024.01.31:false",
            "2023.11.31:2024.11.31:false", "2023.11.21:2023.12.29:false"}, delimiter = ':')
    void calculateExpireDate(@JavaTimeConversionPattern("yyyy.MM.dd") LocalDate now,
                             @JavaTimeConversionPattern("yyyy.MM.dd") LocalDate other,
                             boolean expected) {
        Point point = Point.of(100, "테스트용 포인트", now);

        assertThat(point.isSoonExpireDate(other)).isEqualTo(expected);
    }
}
