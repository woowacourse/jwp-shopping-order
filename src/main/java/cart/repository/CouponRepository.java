package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepository(final CouponDao couponDao, final MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public List<Coupon> findAll() {
        return couponDao.findAll().stream()
                .map(CouponEntity::toDomain)
                .collect(Collectors.toList());
    }

    public List<Coupon> findAllByMemberId(final Long memberId) {
        final List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findByMemberId(memberId);
        final List<Long> couponIds = mapToCouponIds(memberCouponEntities);
        final List<CouponEntity> couponEntities = couponDao.findByIds(couponIds);
        return mapToCoupons(couponEntities);
    }

    private List<Long> mapToCouponIds(final List<MemberCouponEntity> memberCouponEntities) {
        return memberCouponEntities.stream()
                .map(MemberCouponEntity::getCouponId)
                .collect(Collectors.toList());
    }

    private List<Coupon> mapToCoupons(final List<CouponEntity> couponEntities) {
        return couponEntities.stream()
                .map(CouponEntity::toDomain)
                .collect(Collectors.toList());
    }
}
