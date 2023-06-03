package cart.domain.coupon;

import java.util.List;

public interface CouponRepository {

    List<CouponWithId> findAll();

    Coupon findById(final Long id);

    boolean existByNameAndDiscountRate(final String name, final int discountRate);

    long insert(final Coupon coupon);

    void deleteById(final Long couponId);

    CouponWithId findByNameAndDiscountRate(final String name, final int discountRate);
}
