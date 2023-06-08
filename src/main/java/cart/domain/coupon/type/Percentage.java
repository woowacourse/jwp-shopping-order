package cart.domain.coupon.type;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cart.domain.monetary.Discount;

public class Percentage extends CouponInfo {

	private static final BigDecimal PERCENTAGE = BigDecimal.valueOf(100);
	private static final int SCALE = 2;
	private static final RoundingMode HALF_DOWN = RoundingMode.HALF_DOWN;

	public Percentage(final Long id, final String name, final Discount discount) {
		super(id, name, discount);
	}

	@Override
	public BigDecimal calculatePayments(final BigDecimal totalPrice) {
		return totalPrice.multiply(calculateDiscountFactor());
	}

	@Override
	public CouponType getCouponType() {
		return CouponType.PERCENTAGE;
	}

	private BigDecimal calculateDiscountFactor() {
		return BigDecimal.ONE.subtract(computeDiscountPercentage());
	}

	private BigDecimal computeDiscountPercentage() {
		return getDiscount().divide(PERCENTAGE, SCALE, HALF_DOWN);
	}
}
