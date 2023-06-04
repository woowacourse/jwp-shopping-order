package cart.domain.monetary;

import java.math.BigDecimal;

public final class Discount extends MonetaryAmount {

	public Discount(final BigDecimal monetaryAmount) {
		super(monetaryAmount, "할인액");
	}

}
