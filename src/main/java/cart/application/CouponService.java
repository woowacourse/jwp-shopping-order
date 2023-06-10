package cart.application;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.coupon.StrategyFactory;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.dto.coupon.CouponRequest;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final StrategyFactory strategyFactory;

    public CouponService(final CouponRepository couponRepository, final StrategyFactory strategyFactory) {
        this.couponRepository = couponRepository;
        this.strategyFactory = strategyFactory;
    }

    public List<Coupon> findAll() {
        return couponRepository.findAllCoupon();
    }

    public Long create(CouponRequest request) {
        return couponRepository.createCoupon(toCoupon(request));
    }

    public Coupon toCoupon(CouponRequest request) {
        return new Coupon(request.getName(), new Discount(strategyFactory.findStrategy(request.getType()), request.getAmount()));
    }

    public void delete(Long id) {
        couponRepository.deleteCoupon(id);
    }

    public List<MemberCoupon> findUnUsedMemberCouponByMember(Member member) {
        return couponRepository.findUnUsedMemberCouponByMember(member);
    }

    public Long createMemberCoupons(Member member, Long couponId) {
        return couponRepository.createMemberCoupons(member, couponId);
    }

    public List<MemberCoupon> findMemberCouponsByIds(List<Long> ids) {
        return couponRepository.findMemberCouponsByIds(ids);
    }

    public List<MemberCoupon> findMemberCouponsByMemberId(Long memberId) {
        return couponRepository.findMemberCouponsByMemberId(memberId);
    }

    public void updateMemberCoupon(List<MemberCoupon> memberCoupons, Long memberId) {
        couponRepository.updateMemberCoupon(memberCoupons, memberId);
    }
}
