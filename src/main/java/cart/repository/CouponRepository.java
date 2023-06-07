package cart.repository;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.DiscountPolicy;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.discountpolicy.DiscountType;
import cart.persistance.dao.CouponDao;
import cart.persistance.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepository {

    private final DiscountPolicyProvider discountPolicyProvider;
    private final CouponDao couponDao;

    public CouponRepository(final DiscountPolicyProvider discountPolicyProvider, final CouponDao couponDao) {
        this.discountPolicyProvider = discountPolicyProvider;
        this.couponDao = couponDao;
    }

    public Long insert(Coupon coupon) {
        return couponDao.insert(domainToEntity(coupon));
    }

    private CouponEntity domainToEntity(Coupon coupon) {
        DiscountType discountType = discountPolicyProvider.getDiscountType(coupon.getDiscountPolicy());
        return new CouponEntity(
                coupon.getId(),
                coupon.getName(),
                discountType.name(),
                coupon.getDiscountValue()
        );
    }

    public List<Coupon> findAll() {
        return couponDao.findAll().stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    private Coupon entityToDomain(CouponEntity entity) {
        DiscountPolicy discountPolicy = discountPolicyProvider.getByType(DiscountType.valueOf(entity.getDiscountType()));
        return new Coupon(
                entity.getId(),
                entity.getName(),
                discountPolicy,
                entity.getDiscountValue()
        );
    }

    public Coupon findById(Long id) {
        CouponEntity entity = couponDao.findById(id);
        return entityToDomain(entity);
    }

    public List<Coupon> findByIds(final List<Long> ids) {
        return couponDao.findByIds(ids).stream()
                .map(this::entityToDomain)
                .collect(Collectors.toList());
    }

    public void delete(Coupon coupon) {
        couponDao.delete(domainToEntity(coupon));
    }

    public void deleteById(Long id) {
        couponDao.deleteById(id);
    }
}
