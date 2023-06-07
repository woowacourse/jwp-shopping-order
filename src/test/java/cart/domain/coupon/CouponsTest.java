package cart.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static cart.fixture.CouponFixture.createCoupons;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class CouponsTest {

    @DisplayName("id값들을 기준으로 쿠폰을 찾는다.")
    @Test
    void find_coupons_by_ids() {
        // given
        Coupons coupons = createCoupons();

        // when
        List<Coupon> result = coupons.findCouponsByIds(List.of(1L, 2L));

        // then
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getId()).isEqualTo(2L)
        );
    }

    @DisplayName("쿠폰이 정률인 경우, 1~100% 할인율이 아니면 예외를 발생 발생시킨다.")
    @ValueSource(ints = {-1, 0, 101})
    @ParameterizedTest
    void throws_exception_when_amount_invalid(final int amount) {
        // when & then
        assertThatThrownBy(() -> Coupon.create("쿠폰", true, amount));
    }
}
