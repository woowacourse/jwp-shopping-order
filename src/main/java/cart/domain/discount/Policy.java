package cart.domain.discount;

public interface Policy {

    int calculate(final int price);

    void updateDiscountValue(final int value);
}
