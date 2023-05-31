package cart.repository;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountPolicy.DeliveryPolicy;
import cart.domain.coupon.discountPolicy.PercentPolicy;
import cart.domain.coupon.discountPolicy.PricePolicy;
import cart.test.RepositoryTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@RepositoryTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void save() {
        // given
        final Coupon coupon = new Coupon("30000원 이상 3000원 할인 쿠폰", new PricePolicy(), 3000L, new Money(30000L));

        // when
        couponRepository.save(coupon);

        // then
        assertThat(couponRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void findAll() {
        // given
        final Coupon coupon1 = new Coupon("10000원 이상 1000원 할인 쿠폰", new PricePolicy(), 1000L, new Money(10000L));
        final Coupon coupon2 = new Coupon("30000원 이상 10% 할인 쿠폰", new PercentPolicy(), 2000L, new Money(30000L));
        final Coupon coupon3 = new Coupon("50000원 이상 배달비 할인 쿠폰", new DeliveryPolicy(), 0, new Money(50000L));

        // when
        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
        couponRepository.save(coupon3);

        // then
        assertThat(couponRepository.findAll().size()).isEqualTo(3);
    }
}