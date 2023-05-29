package cart.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.CouponFixture.createCoupons;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
}
