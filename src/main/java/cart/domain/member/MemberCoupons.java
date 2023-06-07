package cart.domain.member;

import cart.exception.MemberCouponNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class MemberCoupons {

    private final List<MemberCoupon> coupons;

    public MemberCoupons(final List<MemberCoupon> coupons) {
        this.coupons = coupons;
    }

    public List<MemberCoupon> getCoupons() {
        return coupons;
    }

    public boolean isEmpty() {
        return coupons.isEmpty();
    }

    public MemberCoupons use(MemberCoupons memberCoupons) {
        if (!this.coupons.containsAll(memberCoupons.coupons)) {
            throw new MemberCouponNotFoundException("쿠폰을 보유하고 있지 않습니다.");
        }

        return new MemberCoupons(memberCoupons.coupons.stream()
                .map(MemberCoupon::use)
                .collect(Collectors.toList()));
    }

    public MemberCoupons getUnUsedCoupons() {
        return new MemberCoupons(coupons.stream()
                .filter(MemberCoupon::isUnUsed)
                .collect(Collectors.toList()));
    }
}
