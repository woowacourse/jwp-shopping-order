package cart.domain.coupon.type;

import java.math.BigDecimal;

import cart.domain.monetary.Discount;

public class FixedAmount extends CouponInfo {

	public FixedAmount(final Long id, final String name, final Discount discount) {
		super(id, name, discount);
	}

	@Override
	public BigDecimal calculatePayments(final BigDecimal totalPrice) {
		return totalPrice.subtract(getDiscount());
	}

	@Override
	public String getCouponType() {
		return CouponType.FIXED_AMOUNT.getType();
	}
}
