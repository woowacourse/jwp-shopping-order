package cart.service;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.coupon.IssuableCoupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.repository.CouponRepository;
import cart.domain.coupon.repository.MemberCouponRepository;
import cart.dto.MemberCouponsResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;

    public CouponService(MemberCouponRepository memberCouponRepository, CouponRepository couponRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.couponRepository = couponRepository;
    }

    public MemberCouponsResponse findAll(Member member) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findNotExpired(member);
        return MemberCouponsResponse.from(memberCoupons);
    }

    public void issueByOrderPrice(Money totalOrderPrice, Member member) {
        List<IssuableCoupon> issuableCoupons = findSatisfyIssuableCoupons(totalOrderPrice);
        Money money = getMaxIssueConditionPrice(issuableCoupons);
        List<MemberCoupon> memberCoupons = findSatisfyMemberCoupon(member, issuableCoupons, money);
        if (!memberCoupons.isEmpty()) {
            memberCouponRepository.saveAll(memberCoupons);
        }
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

    private List<MemberCoupon> findSatisfyMemberCoupon(Member member, List<IssuableCoupon> issuableCoupons,
                                                       Money money) {
        return issuableCoupons.stream()
                .filter(issuableCoupon -> issuableCoupon.getMoney().equals(money))
                .map(IssuableCoupon::getCoupon)
                .map(coupon -> new MemberCoupon(member, coupon))
                .collect(Collectors.toList());
    }
}
