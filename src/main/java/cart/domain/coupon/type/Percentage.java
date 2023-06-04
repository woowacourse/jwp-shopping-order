package cart.domain.coupon.type;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cart.domain.monetary.Discount;

public class Percentage extends CouponInfo {

	private static final int PERCENTAGE = 100;
	private static final int SCALE = 2;

	public Percentage(final Long id, final String name, final Discount discount) {
		super(id, name, discount);
	}

	@Override
	public BigDecimal calculatePayments(final BigDecimal totalPrice) {
		return totalPrice.multiply(calculateDiscountFactor());
	}

	@Override
	public String getCouponType() {
		return CouponType.PERCENTAGE.getType();
	}

	private BigDecimal calculateDiscountFactor() {
		return BigDecimal.ONE.subtract(
			getDiscount().divide(BigDecimal.valueOf(PERCENTAGE), SCALE, RoundingMode.HALF_DOWN));
	}
}
