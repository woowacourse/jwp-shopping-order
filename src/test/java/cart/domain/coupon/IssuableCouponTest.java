package cart.domain.coupon;

import cart.domain.Money;
import cart.fixture.TestFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class IssuableCouponTest {

    @ValueSource(ints = {1000, 1001})
    @ParameterizedTest
    void 발급조건에_만족하면_true를_반환한다(int value) {
        IssuableCoupon issuableCoupon = new IssuableCoupon(TestFixture.쿠폰_10퍼센트, new Money(1000));

        boolean result = issuableCoupon.isSatisfied(new Money(value));

        assertThat(result).isTrue();
    }

    @Test
    void 발급_조건이_같으면_true를_반환한다() {
        IssuableCoupon issuableCoupon = new IssuableCoupon(TestFixture.쿠폰_10퍼센트, new Money(1000));

        boolean result = issuableCoupon.isSameCondition(new Money(1000));

        assertThat(result).isTrue();
    }
}
