package cart.domain.coupon.type;

import java.math.BigDecimal;

import cart.domain.monetary.Discount;

public class NotUsed extends CouponInfo {
	public NotUsed() {
		super(0L, "쿠폰 미사용", new Discount(BigDecimal.ZERO));
	}

	@Override
	public BigDecimal calculatePayments(final BigDecimal totalPrice) {
		return totalPrice;
	}

	@Override
	public String getCouponType() {
		return CouponType.NOT_USED.getType();
	}
}
