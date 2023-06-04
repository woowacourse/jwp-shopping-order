package cart.domain.monetary;

import java.math.BigDecimal;
import java.util.Objects;

import cart.error.exception.BadRequestException;

public abstract class MonetaryAmount {

	private final BigDecimal monetaryAmount;

	public MonetaryAmount(final BigDecimal monetaryAmount, final String name) {
		validatePositive(monetaryAmount, name);
		this.monetaryAmount = monetaryAmount;
	}

	private static void validatePositive(final BigDecimal monetaryAmount, final String name) {
		if (monetaryAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new BadRequestException.Monetary(name);
		}
	}

	public final BigDecimal getAmount() {
		return monetaryAmount;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final MonetaryAmount that = (MonetaryAmount)o;
		return Objects.equals(monetaryAmount, that.monetaryAmount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(monetaryAmount);
	}
}
