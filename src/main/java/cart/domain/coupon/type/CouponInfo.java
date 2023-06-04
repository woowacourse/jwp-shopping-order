package cart.domain.coupon.type;

import java.math.BigDecimal;
import java.util.Objects;

import cart.domain.monetary.Discount;

public abstract class CouponInfo {

	private final Long id;
	private final String name;
	private final Discount discount;

	public CouponInfo(final Long id, final String name, final Discount discount) {
		this.id = id;
		this.name = name;
		this.discount = discount;
	}

	public static CouponInfo of(final Long id, final String couponType, final String name,
		final BigDecimal discount) {
		if (Objects.equals(couponType, CouponType.PERCENTAGE.name())) {
			return new Percentage(id, name, new Discount(discount));
		}
		if (Objects.equals(couponType, CouponType.FIXED_AMOUNT.name())) {
			return new FixedAmount(id, name, new Discount(discount));
		}
		return new NotUsed();
	}

	public final Long getId() {
		return id;
	}

	public final String getName() {
		return name;
	}

	public final BigDecimal getDiscount() {
		return discount.getAmount();
	}

	public abstract BigDecimal calculatePayments(final BigDecimal totalPrice);

	public abstract CouponType getCouponType();

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final CouponInfo that = (CouponInfo)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
