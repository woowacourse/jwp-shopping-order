package cart.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Discount Price Calculator 단위 테스트")
class DiscountPriceCalculatorTest {

    private DiscountPriceCalculator discountPriceCalculator;

    @BeforeEach
    void setUp() {
        this.discountPriceCalculator = new DiscountPriceCalculator();
    }

    @Test
    @DisplayName("3만원 미만인 경우 할인되지 않는다.")
    void no_discount_when_under_30000() {
        // given
        final Price price = new Price(20000);

        // when
        final Price discountPrice = discountPriceCalculator.calculate(price);

        // then
        final Price expected = new Price(0);
        assertThat(discountPrice).isEqualTo(expected);
    }

    @Test
    @DisplayName("3만원인 경우 2000원이 할인된다.")
    void discount_2000_when_30000() {
        // given
        final Price price = new Price(30000);

        // when
        final Price discountPrice = discountPriceCalculator.calculate(price);

        // then
        final Price expected = new Price(2000);
        assertThat(discountPrice).isEqualTo(expected);
    }

    @Test
    @DisplayName("3만원 ~ 5만원인 경우 2000원이 할인된다.")
    void discount_2000_when_between_30000_and_50000() {
        // given
        final Price price = new Price(40000);

        // when
        final Price discountPrice = discountPriceCalculator.calculate(price);

        // then
        final Price expected = new Price(2000);
        assertThat(discountPrice).isEqualTo(expected);
    }

    @Test
    @DisplayName("5만원인 경우 5000원이 할인된다.")
    void discount_5000_when_50000() {
        // given
        final Price price = new Price(50000);

        // when
        final Price discountPrice = discountPriceCalculator.calculate(price);

        // then
        final Price expected = new Price(5000);
        assertThat(discountPrice).isEqualTo(expected);
    }

    @Test
    @DisplayName("5만원 초과인 경우 5000원이 할인된다.")
    void discount_5000_when_more_than_50000() {
        // given
        final Price price = new Price(100000);

        // when
        final Price discountPrice = discountPriceCalculator.calculate(price);

        // then
        final Price expected = new Price(5000);
        assertThat(discountPrice).isEqualTo(expected);
    }
}
