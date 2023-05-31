package cart.dto;

public class CouponDto {

    private final Long id;
    private final String name;
    private final String type;
    private final Long discountPrice;
    private final Long minimumPrice;

    public CouponDto(final Long id, final String name, final String type, final Long discountPrice, final Long minimumPrice) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountPrice = discountPrice;
        this.minimumPrice = minimumPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public Long getMinimumPrice() {
        return minimumPrice;
    }
}
