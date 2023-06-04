package cart.entity;

import cart.domain.coupon.DiscountType;

public class CouponEntity {
    private final Long id;
    private final String name;
    private final String discountType;
    private final Integer minimumPrice;
    private final Integer discountPrice;
    private final Double discountRate;

    public static final CouponEntity EMPTY = new CouponEntity(
            null, "", DiscountType.EMPTY_DISCOUNT.getTypeName(),
            0, 0, 0.0
    );

    public CouponEntity(String name, String discountType, Integer minimumPrice, Integer discountPrice, Double discountRate) {
        this(null, name, discountType, minimumPrice, discountPrice, discountRate);
    }

    public CouponEntity(Long id, String name, String discountType, Integer minimumPrice, Integer discountPrice, Double discountRate) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.minimumPrice = minimumPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
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

    public Integer getMinimumPrice() {
        return minimumPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Double getDiscountRate() {
        return discountRate;
    }
}
