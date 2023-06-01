package cart.domain.member;

import java.util.List;

public class MemberCoupons {

    private final List<MemberCoupon> coupons;

    public MemberCoupons(final List<MemberCoupon> coupons) {
        this.coupons = coupons;
    }

    public List<MemberCoupon> getCoupons() {
        return coupons;
    }

    public boolean isNotContains(MemberCoupons memberCoupons) {
        return !this.coupons.containsAll(memberCoupons.coupons);
    }

    public MemberCoupons useCoupons(MemberCoupons memberCoupons){
        this.coupons.removeAll(memberCoupons.coupons);
        return new MemberCoupons(this.coupons);
    }
}
