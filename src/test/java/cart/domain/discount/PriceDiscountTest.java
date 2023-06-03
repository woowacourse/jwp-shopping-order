package cart.domain.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceDiscountTest {

    @ParameterizedTest
    @CsvSource(value = {"100_000", "100_001"})
    void _10만원_이상은_10퍼센트_할인이다(final int price) {
        // given
        final Discount discount = new PriceDiscount(price);

        // expect
        assertThat(discount.getRate()).isEqualTo(0.1);
    }

    @ParameterizedTest
    @CsvSource(value = {"9_999,0.01", "10_000,0.02", "19_999,0.02", "20_000,0.03"})
    void 금액_구간별로_할인율을_적용한다(final int price, final double rate) {
        // given
        final Discount discount = new PriceDiscount(price);

        // expect
        assertThat(discount.getRate()).isEqualTo(rate);
    }

    @ParameterizedTest
    @CsvSource(value = {"10_000,200", "20_000,600", "30_000,1_200", "40_000,2_000"})
    void 할인_금액을_구할_수_있다(final int price, final int discountedPrice) {
        // given
        final Discount discount = new PriceDiscount(price);

        // expect
        assertThat(discount.getMoney()).isEqualTo(discountedPrice);
    }

    @Test
    void 할인_정책_이름을_알_수_있다() {
        // given
        final Discount discount = new PriceDiscount(500);

        // expect
        assertThat(discount.getName()).isEqualTo("priceDiscount");
    }
}
