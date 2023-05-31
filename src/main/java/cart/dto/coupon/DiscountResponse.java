package cart.dto.coupon;

import cart.domain.coupon.Discount;

public class DiscountResponse {

    private String type;
    private int amount;

    private DiscountResponse() {
    }

    public DiscountResponse(final String type, final int amount) {
        this.type = type;
        this.amount = amount;
    }

    public DiscountResponse(final Discount discount) {
        this(discount.getDiscountType().name(), discount.getAmount());
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
