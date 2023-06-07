package cart.domain.member;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.exception.MemberCouponNotFoundException;
import cart.fixture.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberCouponsTest {

    @Test
    void isEmpty() {
        MemberCoupons memberCoupons = new MemberCoupons(List.of(Fixture.멤버_쿠폰, Fixture.멤버_쿠폰2));
        Assertions.assertThat(memberCoupons.isEmpty()).isEqualTo(false);
    }

    @Test
    void isEmptyFail() {
        MemberCoupons memberCoupons = new MemberCoupons(Collections.emptyList());
        Assertions.assertThat(memberCoupons.isEmpty()).isEqualTo(true);
    }

    @Test
    void useCoupons() {
        final MemberCoupon 멤버_쿠폰3 = new MemberCoupon(3L, new Coupon(2L, "오픈 기념 쿠폰", new Discount("rate", 10)), true);
        MemberCoupons memberCoupons = new MemberCoupons(List.of(멤버_쿠폰3));

        List<Boolean> usedMemberCoupons = memberCoupons.use(memberCoupons)
                .getCoupons()
                .stream()
                .map(MemberCoupon::isUsed)
                .collect(Collectors.toList());
        Assertions.assertThat(usedMemberCoupons).isEqualTo(List.of(true));
    }

    @Test
    void useCouponsFail() {
        final MemberCoupon 멤버_쿠폰3 = new MemberCoupon(3L, new Coupon(2L, "오픈 기념 쿠폰", new Discount("rate", 10)), true);
        MemberCoupons memberCoupons = new MemberCoupons(Collections.emptyList());
        MemberCoupons useCoupons = new MemberCoupons(List.of(멤버_쿠폰3));

        assertThatThrownBy(() -> memberCoupons.use(useCoupons)).isInstanceOf(MemberCouponNotFoundException.class);
    }

    @Test
    void getUnUsedCoupons() {
        final MemberCoupon 멤버_쿠폰3 = new MemberCoupon(3L, new Coupon(2L, "오픈 기념 쿠폰", new Discount("rate", 10)), true);
        MemberCoupons memberCoupons = new MemberCoupons(List.of(Fixture.멤버_쿠폰, Fixture.멤버_쿠폰2, 멤버_쿠폰3));

        List<Long> memberCouponIds = memberCoupons.getUnUsedCoupons().getCoupons().stream().map(MemberCoupon::getId).collect(Collectors.toList());

        Assertions.assertThat(memberCouponIds).contains(1L, 2L);
    }
}
