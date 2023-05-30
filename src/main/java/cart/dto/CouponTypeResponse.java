package cart.dto;

public class CouponTypeResponse {

    private final Long id;
    private final String name;
    private final int discountAmount;
    private final String description;

    public CouponTypeResponse(final Long id, final String name, final int discountAmount, final String description) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public String getDescription() {
        return description;
    }
}
