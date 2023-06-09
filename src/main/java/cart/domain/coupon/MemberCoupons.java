package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.discountpolicy.DiscountType;

import java.util.List;
import java.util.stream.Collectors;

public class MemberCoupons {

    private final List<MemberCoupon> memberCoupons;

    private MemberCoupons(List<MemberCoupon> memberCoupons) {
        this.memberCoupons = memberCoupons;
    }

    public static MemberCoupons of(List<MemberCoupon> memberCoupons, DiscountPolicyProvider discountPolicyProvider) {
        final List<MemberCoupon> orderedCoupons = memberCoupons.stream()
                .sorted((coupon1, coupon2) -> {
                    DiscountType coupon1type = discountPolicyProvider.getDiscountType(coupon1.getDiscountPolicy());
                    DiscountType coupon2type = discountPolicyProvider.getDiscountType(coupon2.getDiscountPolicy());
                    return coupon1type.getPriority() - coupon2type.getPriority();
                })
                .collect(Collectors.toUnmodifiableList());
        return new MemberCoupons(orderedCoupons);
    }

    public Money apply(Money originalPrice) {
        Money afterDiscountMoney = new Money(originalPrice.getValue());
        for (MemberCoupon memberCoupon : memberCoupons) {
            afterDiscountMoney = memberCoupon.use(afterDiscountMoney);
        }
        return afterDiscountMoney;
    }

    public List<MemberCoupon> getMemberCoupons() {
        return memberCoupons;
    }
}
