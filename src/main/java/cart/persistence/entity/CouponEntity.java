package cart.persistence.entity;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponInfo;
import cart.domain.coupon.CouponType;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final int minPrice;
    private final int maxPrice;
    private final CouponType type;
    private final Integer discountAmount;
    private final Double discountPercentage;

    public CouponEntity(final String name, final int minPrice, final int maxPrice, final CouponType type,
            final Integer discountAmount, final Double discountPercentage) {
        this(null, name, minPrice, maxPrice, type, discountAmount, discountPercentage);
    }

    public CouponEntity(final Long id, final String name, final int minPrice, final int maxPrice, final CouponType type,
            final Integer discountAmount, final Double discountPercentage) {
        this.id = id;
        this.name = name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.type = type;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
    }

    public Coupon toDomain() {
        CouponInfo couponInfo = new CouponInfo(id, name, minPrice, maxPrice);
        int value = discountAmount;
        if (type == CouponType.PERCENT) {
            value = discountPercentage.intValue();
        }
        return new Coupon(couponInfo, value, type);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public CouponType getType() {
        return type;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }
}
