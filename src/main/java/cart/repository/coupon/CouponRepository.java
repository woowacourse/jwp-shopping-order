package cart.repository.coupon;

import cart.dao.coupon.CouponDao;
import cart.dao.policy.PolicyDao;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.discount.Policy;
import cart.domain.discount.PolicyDiscount;
import cart.domain.discount.PolicyPercentage;
import cart.entity.coupon.CouponEntity;
import cart.entity.policy.PolicyEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final PolicyDao policyDao;

    public CouponRepository(final CouponDao couponDao, final PolicyDao policyDao) {
        this.couponDao = couponDao;
        this.policyDao = policyDao;
    }

    public Coupons findAllByMemberId(final long memberId) {
        List<CouponEntity> couponEntities = couponDao.findAllCouponEntitiesByMemberId(memberId);

        return makeCoupons(couponEntities);
    }

    public Coupons findAllByCouponIds(final List<Long> couponIds) {
        List<CouponEntity> couponEntities = couponIds.stream()
                .map(couponDao::findById)
                .collect(Collectors.toList());

        return makeCoupons(couponEntities);
    }

    private Coupons makeCoupons(final List<CouponEntity> couponEntities) {
        List<Coupon> coupons = couponEntities.stream()
                .map(this::createCoupon)
                .collect(Collectors.toList());

        return new Coupons(coupons);
    }

    private Coupon createCoupon(final CouponEntity couponEntity) {
        PolicyEntity policyEntity = policyDao.findById(couponEntity.getPolicyId());
        Policy policy = determinePolicy(policyEntity);
        return new Coupon(couponEntity.getId(), couponEntity.getName(), policy);
    }

    private Policy determinePolicy(final PolicyEntity policyEntity) {
        if (policyEntity.isPercentage()) {
            return new PolicyPercentage(policyEntity.getAmount());
        }

        return new PolicyDiscount(policyEntity.getAmount());
    }


    public void deleteById(final long couponId) {
        couponDao.deleteById(couponId);
    }

    public void deleteAllByIds(final List<Long> couponIds) {
        couponDao.deleteAllByIds(couponIds);
    }
}
