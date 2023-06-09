package cart.repository;

import cart.dao.CouponDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.CouponTypeCouponResultMap;
import cart.dao.entity.CouponTypeEntity;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.exception.internal.NoCouponException;
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
    public void changeStatusTo(final Long couponId, final Boolean toChange) {
        couponDao.changeStatus(couponId, toChange);
    }

    @Override
    public Coupons findCouponsByMemberId(final Long memberId) {
        final List<CouponTypeCouponResultMap> couponTypeCouponResultMap = couponDao.findByMemberId(memberId);
        final List<Coupon> coupons = couponTypeCouponResultMap.stream()
                .map(CouponJdbcRepository::toDomain)
                .collect(toList());

        return new Coupons(coupons);
    }

    @Override
    public Coupons findCouponAll() {
        final List<CouponTypeEntity> couponTypeEntities = couponDao.findAll();
        final List<Coupon> coupons = couponTypeEntities.stream()
                .map(it -> Coupon.createCouponType(it.getId(), it.getName(), it.getDescription(), it.getDiscountAmount()))
                .collect(toList());
        return new Coupons(coupons);
    }

    @Override
    public void deleteCoupon(final Long id) {
        couponDao.deleteCoupon(id);
    }

    @Override
    public Coupon findCouponById(final Long couponId) {
        final CouponTypeCouponResultMap resultMap = couponDao.findById(couponId)
                .orElseThrow(NoCouponException::new);
        return toDomain(resultMap);
    }

    private static Coupon toDomain(final CouponTypeCouponResultMap resultMap) {
        return new Coupon(
                resultMap.getCouponId(),
                resultMap.getCouponTypeId(),
                resultMap.getName(),
                resultMap.getDescription(),
                resultMap.getDiscountAmount(),
                resultMap.getUsageStatus()
        );
    }
}
