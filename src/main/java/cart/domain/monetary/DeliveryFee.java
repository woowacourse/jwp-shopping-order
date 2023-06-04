package cart.domain.monetary;

import java.math.BigDecimal;

public final class DeliveryFee extends MonetaryAmount {

	public static final BigDecimal DEFAULT = BigDecimal.valueOf(3_000L);

	public DeliveryFee(final BigDecimal monetaryAmount) {
		super(monetaryAmount, "배송비");
	}

	public DeliveryFee() {
		super(DEFAULT, "배송비");
	}

}
