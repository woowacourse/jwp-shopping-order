package cart.coupon.application;

import cart.coupon.Coupon;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.member.application.MemberCouponMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {
    private final MemberCouponMapper memberCouponMapper = new MemberCouponMapper();
    private final Map<Long, Coupon> couponMap = new HashMap<>();
    private long id = 1L;

    public Long save(String name, Long discountPolicyId) {
        final var id = this.id++;
        this.couponMap.put(id, new Coupon(id, name, discountPolicyId));
        return id;
    }

    public Coupon findById(Long id) {
        return this.couponMap.get(id);
    }

    public List<Coupon> findAllByMemberId(Long memberId) {
        return memberCouponMapper.findAllByMemberId(memberId)
                .stream().map(couponMap::get)
                .collect(Collectors.toList());
    }

    public void giveCouponToMember(Long memberId, Long couponId) {
        this.memberCouponMapper.addCoupon(memberId, couponId);
    }

    public List<Coupon> findAllByMemberIdExcludingTarget(Long memberId, List<DiscountTarget> discountTargets) {
        return null;
    }

    public List<Coupon> findAllByMemberIdApplyingToTotalPrice(Long memberId) {
        return null;
    }
}
