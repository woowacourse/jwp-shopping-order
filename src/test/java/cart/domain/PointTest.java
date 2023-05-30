package cart.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PointTest {

    @Test
    void 포인트를_차감한다() {
        //given
        final Point point = Point.valueOf(1000);

        //when
        final Point reducedPoint = point.reduce(Point.valueOf(100));

        //then
        assertThat(reducedPoint).isEqualTo(Point.valueOf(900));
    }

    @ParameterizedTest
    @CsvSource({"999, true", "1000, false", "1001, false"})
    void 포인트가_더_많은지_비교한다(final int amount, final boolean expected) {
        //given
        final Point point = Point.valueOf(1000);

        //when
        final boolean actual = point.isMoreThan(Point.valueOf(amount));

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
