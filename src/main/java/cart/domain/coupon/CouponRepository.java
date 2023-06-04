package cart.domain.coupon;

import java.util.List;

import cart.domain.coupon.type.CouponInfo;

public interface CouponRepository {
	Long createCoupon(CouponInfo couponInfo);

	List<CouponInfo> findAll();

	Coupon findCouponById(Long couponId);

	void updateCouponInfo(CouponInfo couponInfo);

	void removeCoupon(Long couponId);
}
