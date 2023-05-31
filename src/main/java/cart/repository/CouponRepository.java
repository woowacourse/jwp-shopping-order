package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.MemberCouponEntity;
import cart.domain.Coupon;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {
    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepository(final CouponDao couponDao, final MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public List<Coupon> findCouponByMember(final Member member) {
        // TODO: 5/30/23 JOIN 사용할지 고민해보기
        Map<Long, CouponEntity> allCouponsById = couponDao.findAll().stream()
                .collect(Collectors.toMap(CouponEntity::getId, couponEntity -> couponEntity));

        return memberCouponDao.findUsableByMemberId(member.getId()).stream()
                .map(memberCouponEntity -> memberCouponEntity.toCoupon(allCouponsById))
                .collect(Collectors.toList());
    }

    public Coupon findById(final Long id) {
        MemberCouponEntity memberCouponEntity = memberCouponDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버의 쿠폰이 존재하지 않습니다."));
        CouponEntity couponEntity = couponDao.findById(memberCouponEntity.getCouponId())
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다."));
        return memberCouponEntity.toCoupon(couponEntity);
    }

    // TODO: 5/31/23 예외 컨트롤
    public void delete(final Coupon coupon) {
        memberCouponDao.updateUsedById(coupon.getCouponInfo().getId());
    }
}
