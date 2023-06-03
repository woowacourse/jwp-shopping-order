package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.DiscountPolicyType;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.MemberEntity;
import cart.exception.CouponNotFoundException;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.MemberNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;
    private final CouponDao couponDao;
    private final MemberDao memberDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao, final CouponDao couponDao,
                                  final MemberDao memberDao) {
        this.memberCouponDao = memberCouponDao;
        this.couponDao = couponDao;
        this.memberDao = memberDao;
    }

    public List<MemberCoupon> findAllByMemberId(final Long memberId) {
        final Member member = getMember(memberId);
        final List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findAllNotUsedMemberCouponByMemberId(
                memberId);

        return memberCouponEntities.stream().map(it -> {
            Coupon coupon = getCoupon(it.getCouponId());
            return new MemberCoupon(it.getId(), member, coupon, it.getUsed());
        }).collect(Collectors.toList());
    }

    public MemberCoupon findById(final Long memberCouponId) {
        final MemberCouponEntity findMemberCoupon = memberCouponDao.findMemberCouponById(memberCouponId)
                .orElseThrow(MemberCouponNotFoundException::new);

        final Member member = getMember(findMemberCoupon.getMemberId());
        final Coupon coupon = getCoupon(findMemberCoupon.getCouponId());
        return new MemberCoupon(findMemberCoupon.getId(), member, coupon, findMemberCoupon.getUsed());
    }

    private Member getMember(final Long memberId) {
        final MemberEntity memberEntity = memberDao.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return memberEntity.toDomain();
    }

    private Coupon getCoupon(final Long couponId) {
        final CouponEntity couponEntity = couponDao.findByCouponId(couponId).orElseThrow(CouponNotFoundException::new);
        final DiscountPolicy discountPolicy = DiscountPolicyType.findDiscountPolicy(couponEntity.getPolicyType(),
                couponEntity.getDiscountPrice());

        return new Coupon(couponEntity.getId(), couponEntity.getName(), discountPolicy, couponEntity.getMinimumPrice());
    }

    public void useMemberCoupon(final Long memberCouponId) {
        memberCouponDao.updateUsedCouponById(memberCouponId);
    }
}
