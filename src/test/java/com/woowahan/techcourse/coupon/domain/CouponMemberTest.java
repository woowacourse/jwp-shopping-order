package com.woowahan.techcourse.coupon.domain;

import static com.woowahan.techcourse.coupon.domain.CouponFixture.COUPON1;
import static com.woowahan.techcourse.coupon.domain.CouponFixture.COUPON2;
import static com.woowahan.techcourse.coupon.domain.CouponFixture.COUPON3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.coupon.exception.CouponException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
@DisplayNameGeneration(ReplaceUnderscores.class)
class CouponMemberTest {

    private CouponMember couponMember;

    @BeforeEach
    void setUp() {
        couponMember = new CouponMember(1L, List.of(COUPON1, COUPON2, COUPON3));
    }

    @Test
    void 멤버_id를_잘_가져온다() {
        assertThat(couponMember.getMemberId()).isEqualTo(1L);
    }

    @Test
    void 멤버가_쿠폰을_가지고_있는지_확인한다() {
        assertSoftly(softly -> {
            softly.assertThat(couponMember.hasCoupon(COUPON1)).isTrue();
            softly.assertThat(couponMember.hasCoupon(COUPON2)).isTrue();
            softly.assertThat(couponMember.hasCoupon(COUPON3)).isTrue();
        });
    }

    @Test
    void 멤버가_모든_쿠폰을_가지고_있는지_확인한다() {
        assertThat(couponMember.hasAllCouponIds(
                List.of(COUPON1.getCouponId(), COUPON2.getCouponId(), COUPON3.getCouponId()))).isTrue();
    }

    @Test
    void 멤버가_모든_쿠폰을_가지고_있지_않은_경우_확인한다() {
        assertThat(couponMember.hasAllCouponIds(
                List.of(COUPON1.getCouponId(), COUPON2.getCouponId(), 4L))).isFalse();
    }

    @Test
    void 멤버가_가진_쿠폰을_만료시키고_보유_쿠폰을_확인한다() {
        couponMember.expireCouponIds(List.of(COUPON1.getCouponId(), COUPON2.getCouponId()));
        assertSoftly(softly -> {
            softly.assertThat(couponMember.hasCoupon(COUPON1)).isFalse();
            softly.assertThat(couponMember.hasCoupon(COUPON2)).isFalse();
            softly.assertThat(couponMember.hasCoupon(COUPON3)).isTrue();
        });
    }

    @Test
    void 없는_쿠폰을_만료시키면_예외가_발생한다() {
        assertThatThrownBy(() -> couponMember.expireCouponIds(List.of(4L)))
                .isInstanceOf(CouponException.class);
    }

    @Test
    void 같은_쿠폰을_중복_추가하면_예외가_발생한다() {
        assertThatThrownBy(() -> couponMember.addCoupon(COUPON1))
                .isInstanceOf(CouponException.class);
    }

    @Test
    void 멤버가_모든_쿠폰을_가지고_있지_않는지_체크한다() {
        assertThat(couponMember.notHasAllCouponIds(
                List.of(COUPON1.getCouponId(), COUPON2.getCouponId(), 4L))).isTrue();
    }
}
