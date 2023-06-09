package cart.domain.monetary;

import java.math.BigDecimal;

public class Price extends MonetaryAmount{
	public Price(final BigDecimal monetaryAmount) {
		super(monetaryAmount, "가격");
	}

}
