package cart.persistence.entity;

public class CouponEntity {
    private final Long id;
    private final String name;
    private final String discountType;
    private final Double discountPercent;
    private final Integer discountAmount;
    private final Integer minimumPrice;

    public CouponEntity(Long id, String name, String discountType, Double discountPercent, Integer discountAmount, Integer minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }
}
