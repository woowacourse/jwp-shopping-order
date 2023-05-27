package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepository(final CouponDao couponDao, final MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Coupon save(final Coupon coupon) {
        final CouponEntity couponEntity = CouponEntity.from(coupon);
        final CouponEntity savedCouponEntity = couponDao.insert(couponEntity);
        return new Coupon(
                savedCouponEntity.getId(),
                coupon.getName(),
                coupon.getDiscountPolicy(),
                coupon.getDiscountCondition()
        );
    }

    public List<Coupon> findAllByMemberId(final Long memberId) {
        final List<Long> couponIds = memberCouponDao.findAllUnusedMemberCouponByMemberId(memberId).stream()
                .map(MemberCouponEntity::getCouponId)
                .collect(toList());
        return couponDao.findByIds(couponIds).stream()
                .map(CouponEntity::toDomain)
                .collect(toList());
    }

    public Optional<Coupon> findById(final Long id) {
        return couponDao.findById(id).map(CouponEntity::toDomain);
    }

    public Optional<Coupon> findByIdAndMemberId(final Long id, final Long memberId) {
        final boolean invalidCoupon = memberCouponDao.findAllUnusedMemberCouponByMemberId(memberId).stream()
                .map(MemberCouponEntity::getCouponId)
                .noneMatch(couponId -> couponId.equals(id));
        if (invalidCoupon) {
            return Optional.empty();
        }
        return couponDao.findById(id).map(CouponEntity::toDomain);
    }
}
