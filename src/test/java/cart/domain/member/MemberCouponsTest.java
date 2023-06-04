package cart.domain.member;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.fixture.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class MemberCouponsTest {

    @Test
    void isNotContains() {
        MemberCoupons memberCoupons = new MemberCoupons(List.of(Fixture.멤버_쿠폰, Fixture.멤버_쿠폰2));
        Assertions.assertThat(memberCoupons.isNotContains(new MemberCoupons(List.of(Fixture.멤버_쿠폰)))).isEqualTo(false);
    }

    @Test
    void isNotContainsFail() {
        MemberCoupons memberCoupons = new MemberCoupons(List.of(Fixture.멤버_쿠폰2));
        Assertions.assertThat(memberCoupons.isNotContains(new MemberCoupons(List.of(Fixture.멤버_쿠폰)))).isEqualTo(true);
    }

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

        List<Boolean> usedMemberCoupons = memberCoupons.use()
                .getCoupons()
                .stream()
                .map(MemberCoupon::isUsed)
                .collect(Collectors.toList());
        Assertions.assertThat(usedMemberCoupons).isEqualTo(List.of(true));
    }

    @Test
    void getUnUsedCoupons() {
        final MemberCoupon 멤버_쿠폰3 = new MemberCoupon(3L, new Coupon(2L, "오픈 기념 쿠폰", new Discount("rate", 10)), true);
        MemberCoupons memberCoupons = new MemberCoupons(List.of(Fixture.멤버_쿠폰, Fixture.멤버_쿠폰2, 멤버_쿠폰3));

        List<Long> memberCouponIds = memberCoupons.getUnUsedCoupons().getCoupons().stream().map(MemberCoupon::getId).collect(Collectors.toList());

        Assertions.assertThat(memberCouponIds).contains(1L, 2L);
    }
}
