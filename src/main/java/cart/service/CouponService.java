package cart.service;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.coupon.IssuableCoupon;
import cart.domain.coupon.MemberCoupon;
import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
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

    public CouponResponse findAllByMember(Member member) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findNotExpiredAllByMember(member);
        return CouponResponse.from(memberCoupons);
    }

    public void issueByOrderPrice(Money totalOrderPrice, Member member) {
        List<IssuableCoupon> issuableCoupons = getSatisfyIssuableCoupons(totalOrderPrice);
        Money money = getMaxIssueConditionPrice(issuableCoupons);
        List<MemberCoupon> memberCoupons = getSatisfyMemberCoupon(member, issuableCoupons, money);
        if (!memberCoupons.isEmpty()) {
            memberCouponRepository.saveAll(memberCoupons);
        }
    }

    private List<IssuableCoupon> getSatisfyIssuableCoupons(Money totalOrderPrice) {
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

    private List<MemberCoupon> getSatisfyMemberCoupon(Member member, List<IssuableCoupon> issuableCoupons,
                                                      Money money) {
        return issuableCoupons.stream()
                .filter(issuableCoupon -> issuableCoupon.getMoney().equals(money))
                .map(IssuableCoupon::getCoupon)
                .map(coupon -> new MemberCoupon(member, coupon))
                .collect(Collectors.toList());
    }
}
