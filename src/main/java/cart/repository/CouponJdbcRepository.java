package cart.repository;

import cart.dao.CouponDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.CouponTypeCouponEntity;
import cart.dao.entity.CouponTypeEntity;
import cart.domain.Coupon;
import cart.domain.Coupons;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class CouponJdbcRepository implements CouponRepository {

    private final CouponDao couponDao;

    public CouponJdbcRepository(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public Long issue(final Member member, final Long couponId) {
        final CouponEntity couponEntity = toEntity(member, couponId);
        return couponDao.issue(couponEntity);
    }

    private CouponEntity toEntity(final Member member, final Long couponId) {
        return CouponEntity.of(member.getId(), couponId);
    }

    @Override
    public void changeStatus(final Long couponId, final Long memberId) {
        couponDao.changeStatus(couponId, memberId);
    }

    @Override
    public Coupons findCouponsByMemberId(final Long memberId) {
        final List<CouponTypeCouponEntity> couponTypeCouponEntities = couponDao.findByMemberId(memberId);
        final List<Coupon> coupons = couponTypeCouponEntities.stream()
                .map(CouponJdbcRepository::toDomain)
                .collect(toList());

        return new Coupons(coupons);
    }

    private static Coupon toDomain(final CouponTypeCouponEntity entity) {
        return new Coupon(
                entity.getCouponId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDiscountAmount(),
                entity.getUsageStatus()
        );
    }

    @Override
    public Coupons findCouponAll() {
        final List<CouponTypeEntity> couponTypeEntities = couponDao.findAll();
        final List<Coupon> coupons = couponTypeEntities.stream()
                .map(it -> Coupon.createCouponType(it.getId(), it.getName(), it.getDescription(), it.getDiscountAmount()))
                .collect(toList());
        return new Coupons(coupons);
    }
}
