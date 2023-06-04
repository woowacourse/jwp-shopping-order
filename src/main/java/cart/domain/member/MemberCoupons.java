package cart.domain.member;

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

    public boolean isNotContains(MemberCoupons memberCoupons) {
        return !this.coupons.containsAll(memberCoupons.coupons);
    }

    public boolean isEmpty(){
        return coupons.isEmpty();
    }

    public MemberCoupons use() {
        return new MemberCoupons(coupons.stream()
                .map(MemberCoupon::use)
                .collect(Collectors.toList()));
    }

    public MemberCoupons getUnUsedCoupons() {
        return new MemberCoupons(coupons.stream()
                .filter(MemberCoupon::isUnUsed)
                .collect(Collectors.toList()));
    }
}
