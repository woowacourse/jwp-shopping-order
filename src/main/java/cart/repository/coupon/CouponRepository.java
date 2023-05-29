package cart.repository.coupon;

import cart.dao.coupon.CouponDao;
import cart.dao.policy.PolicyDao;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.discount.PolicyDiscount;
import cart.domain.discount.PolicyPercentage;
import cart.entity.coupon.CouponEntity;
import cart.entity.policy.PolicyEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        List<PolicyEntity> policyEntities = couponEntities.stream()
                .map(couponEntity -> policyDao.findById(couponEntity.getPolicyId()))
                .collect(Collectors.toList());

        List<Coupon> coupons = new ArrayList<>();

        for (int i = 0; i < couponEntities.size(); i++) {
            boolean isPercentage = policyEntities.get(i).isPercentage();

            if (isPercentage) {
                coupons.add(new Coupon(couponEntities.get(i).getId(), couponEntities.get(i).getName(), new PolicyPercentage(policyEntities.get(i).getAmount())));
                continue;
            }

            coupons.add(new Coupon(couponEntities.get(i).getId(), couponEntities.get(i).getName(), new PolicyDiscount(policyEntities.get(i).getAmount())));
        }

        return new Coupons(coupons);
    }

    public void deleteById(final long couponId) {
        couponDao.deleteById(couponId);
    }
}
