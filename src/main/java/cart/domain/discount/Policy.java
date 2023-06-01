package cart.domain.discount;

public interface Policy {

    int calculate(final int price);

    int updateDiscountValue(final int value);
}
