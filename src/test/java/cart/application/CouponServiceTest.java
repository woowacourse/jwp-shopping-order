package cart.application;

import cart.dto.CouponResponse;
import cart.dto.CouponsResponse;
import cart.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.CouponFixture.RATE_10_COUPON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@AutoConfigureTestDatabase
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    private Long rateCouponId;
    private Long amountCouponId;

    @BeforeEach
    void setUp() {
        rateCouponId = couponRepository.insert(RATE_10_COUPON);
        amountCouponId = couponRepository.insert(AMOUNT_1000_COUPON);
    }

    @Test
    @DisplayName("쿠폰들 정보를 조회한다.")
    void find_all_coupon_test() {
        // given

        // when
        CouponsResponse couponResponse = couponService.getAllCoupons();

        // then
        List<Long> ids = couponResponse.getCoupons().stream()
                .map(CouponResponse::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactlyInAnyOrder(rateCouponId, amountCouponId);
    }
}
