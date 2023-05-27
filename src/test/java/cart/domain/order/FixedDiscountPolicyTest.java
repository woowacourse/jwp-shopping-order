package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FixedDiscountPolicyTest {

    @DisplayName("5만원 미만의 금액은 할인되지 않는다.")
    @ValueSource(ints = {0, 25_000, 49_999})
    @ParameterizedTest
    void discount_priceUnder50000_noDiscount() {
        // given
        final Price price = new Price(49_999);
        final FixedDiscountPolicy fixedDiscountPolicy = FixedDiscountPolicy.from(price);
        final int discountPrice = fixedDiscountPolicy.getDiscountPrice();

        // when
        final Price discountedPrice = fixedDiscountPolicy.discount(price);

        // then
        assertThat(discountPrice).isZero();
        assertThat(discountedPrice.getValue()).isEqualTo(price.getValue());
    }

    @DisplayName("5만원 이상의 금액은 2000원 할인된다.")
    @ValueSource(ints = {50_000, 80_000, 99_999})
    @ParameterizedTest
    void discount_price50000OrMore_2000discount(int value) {
        // given
        final Price price = new Price(value);
        final FixedDiscountPolicy fixedDiscountPolicy = FixedDiscountPolicy.from(price);
        final int discountPrice = fixedDiscountPolicy.getDiscountPrice();

        // when
        final Price discountedPrice = fixedDiscountPolicy.discount(price);

        // then
        assertThat(discountPrice).isEqualTo(2_000);
        assertThat(discountedPrice.getValue()).isEqualTo(price.getValue() - discountPrice);
    }

    @DisplayName("10만원 이상의 금액은 5000원 할인된다.")
    @ValueSource(ints = {100_000, 150_000, 199_999})
    @ParameterizedTest
    void discount_price100000OrMore_5000discount(int value) {
        // given
        final Price price = new Price(value);
        final FixedDiscountPolicy fixedDiscountPolicy = FixedDiscountPolicy.from(price);
        final int discountPrice = fixedDiscountPolicy.getDiscountPrice();

        // when
        final Price discountedPrice = fixedDiscountPolicy.discount(price);

        // then
        assertThat(discountPrice).isEqualTo(5_000);
        assertThat(discountedPrice.getValue()).isEqualTo(price.getValue() - discountPrice);
    }

    @DisplayName("20만원 이상의 금액은 12000원 할인된다.")
    @ValueSource(ints = {200_000, 500_000, 1_000_000})
    @ParameterizedTest
    void discount_price200000OrMore_12000discount(int value) {
        // given
        final Price price = new Price(value);
        final FixedDiscountPolicy fixedDiscountPolicy = FixedDiscountPolicy.from(price);
        final int discountPrice = fixedDiscountPolicy.getDiscountPrice();

        // when
        final Price discountedPrice = fixedDiscountPolicy.discount(price);

        // then
        assertThat(discountPrice).isEqualTo(12_000);
        assertThat(discountedPrice.getValue()).isEqualTo(price.getValue() - discountPrice);
    }
}
