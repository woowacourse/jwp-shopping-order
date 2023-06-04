package cart.domain.discount;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Grade;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GradeDiscountTest {


    @ParameterizedTest
    @CsvSource(value = {"GOLD,0.05", "SILVER,0.03", "BRONZE,0.01"})
    void GOLD_는_5퍼센트_SILVER_는_3퍼센트_BRONZE_는_1퍼센트_할인한다(final String gradeName, final double rate) {
        // given
        final Discount discount = new GradeDiscount(Grade.from(gradeName), 1000);

        // expect
        assertThat(discount.getDiscountRate()).isEqualTo(rate);
    }

    @ParameterizedTest
    @CsvSource(value = {"GOLD,5_000", "SILVER,3_000", "BRONZE,1_000"})
    void 할인_금액을_구할_수_있다(final String gradeName, final int discountedPrice) {
        // given
        final int originPrice = 100_000;

        // when
        final Discount discount = new GradeDiscount(Grade.from(gradeName), originPrice);

        // then
        assertThat(discount.getDiscountedPrice()).isEqualTo(discountedPrice);
    }

    @Test
    void 할인_정책_이름을_알_수_있다() {
        // given
        final Discount discount = new GradeDiscount(Grade.from("GOLD"), 10_000);

        // expect
        assertThat(discount.getName()).isEqualTo("memberGradeDiscount");
    }
}
