package cart.service;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.coupon.AmountDiscountPolicy;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DeliveryFeeDiscountPolicy;
import cart.domain.coupon.MinimumPriceDiscountCondition;
import cart.domain.coupon.NoneDiscountCondition;
import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 쿠폰을_전체_조회한다() {
        // given
        final Coupon coupon1 = couponRepository.save(new Coupon(
                "2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        ));
        final Coupon coupon2 = couponRepository.save(new Coupon(
                "30000원 이상 배달비 할인 쿠폰",
                new DeliveryFeeDiscountPolicy(),
                new MinimumPriceDiscountCondition(30000)
        ));

        // when
        final List<CouponResponse> result = couponService.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new CouponResponse(
                        coupon1.getId(),
                        "2000원 할인 쿠폰",
                        "price",
                        2000L,
                        0,
                        false,
                        0
                ),
                new CouponResponse(
                        coupon2.getId(),
                        "30000원 이상 배달비 할인 쿠폰",
                        "delivery",
                        0L,
                        0,
                        true,
                        30000
                )
        ));
    }
}
