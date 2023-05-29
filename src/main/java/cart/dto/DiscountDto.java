package cart.dto;

import cart.domain.Discount;

public class DiscountDto {

    private final String type;
    private final int amount;

    public DiscountDto(final String type, final int amount) {
        this.type = type;
        this.amount = amount;
    }

    public DiscountDto(final Discount discount) {
        this(discount.getDiscountType().name(), discount.getAmount());
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
