package cart.persistence.coupon;

import java.math.BigDecimal;

import cart.domain.coupon.SerialNumber;
import cart.domain.coupon.type.CouponInfo;

public class CouponJoinSerialNumber {

	private final CouponInfo couponInfo;
	private final SerialNumber serialNumber;

	public CouponJoinSerialNumber(final Long couponId, final String couponType, final String name,
		final BigDecimal discount, final Long serialNumberId, final String serialNumber, final boolean isIssued) {
		this.couponInfo = CouponInfo.of(couponId, couponType, name, discount);
		this.serialNumber = new SerialNumber(serialNumberId, serialNumber, isIssued);
	}

	public CouponInfo getCouponInfo() {
		return couponInfo;
	}

	public SerialNumber getSerialNumber() {
		return serialNumber;
	}
}
