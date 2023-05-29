package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class AmountDiscountRangeTest {

    @ParameterizedTest
    @CsvSource(value = {"10000:0", "30000:2000", "50000:5000"}, delimiter = ':')
    void 할인_금액_계산_테스트(final int price, final int expectedDiscountAmount) {
        final int discountAmount = AmountDiscountRange.findDiscountAmount(price);

        assertThat(discountAmount).isEqualTo(expectedDiscountAmount);
    }
}
