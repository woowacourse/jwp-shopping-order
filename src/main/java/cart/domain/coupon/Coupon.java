package cart.domain.coupon;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import cart.domain.coupon.type.CouponInfo;

public class Coupon {

	private final CouponInfo couponInfo;
	private final List<SerialNumber> serialNumbers;

	public Coupon(final CouponInfo couponInfo, final List<SerialNumber> serialNumbers) {
		this.couponInfo = couponInfo;
		this.serialNumbers = serialNumbers;
	}

	public Optional<SerialNumber> findUnissuedSerialNumber() {
		return serialNumbers.stream()
			.filter(Predicate.not(SerialNumber::isIssued))
			.findFirst();
	}

	public CouponInfo getCouponInfo() {
		return couponInfo;
	}

	public List<SerialNumber> getSerialNumbers() {
		return serialNumbers;
	}
}
