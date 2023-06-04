package cart.service;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.IssuableCoupon;
import cart.domain.coupon.repository.CouponRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponIssuer {

    private final CouponRepository couponRepository;

    public CouponIssuer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<Coupon> issueAllCoupons(Money totalOrderPrice) {
        List<IssuableCoupon> satisfyIssuableCoupons = findSatisfyIssuableCoupons(totalOrderPrice);
        Money maxIssueConditionPrice = getMaxIssueConditionPrice(satisfyIssuableCoupons);
        return findAllSatisfyMemberCoupon(satisfyIssuableCoupons, maxIssueConditionPrice);
    }

    private List<IssuableCoupon> findSatisfyIssuableCoupons(Money totalOrderPrice) {
        return couponRepository.findAllIssuable().stream()
                .filter(coupon -> coupon.isSatisfied(totalOrderPrice))
                .collect(Collectors.toList());
    }

    private Money getMaxIssueConditionPrice(List<IssuableCoupon> issuableCoupons) {
        return issuableCoupons.stream()
                .map(IssuableCoupon::getMoney)
                .max(Comparator.comparing(Money::getValue))
                .orElse(Money.ZERO);
    }

    private List<Coupon> findAllSatisfyMemberCoupon(List<IssuableCoupon> issuableCoupons, Money money) {
        return issuableCoupons.stream()
                .filter(issuableCoupon -> issuableCoupon.isSameCondition(money))
                .map(IssuableCoupon::getCoupon)
                .collect(Collectors.toList());
    }
}
