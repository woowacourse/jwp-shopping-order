package cart.persistence.entity;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponInfo;
import cart.domain.coupon.CouponType;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final int minOrderPrice;
    private final int maxDiscountPrice;
    private final CouponType type;
    private final Integer discountAmount;
    private final Double discountPercentage;

    public CouponEntity(final String name, final int minOrderPrice, final int maxDiscountPrice, final CouponType type,
            final Integer discountAmount, final Double discountPercentage) {
        this(null, name, minOrderPrice, maxDiscountPrice, type, discountAmount, discountPercentage);
    }

    public CouponEntity(final Long id, final String name, final int minOrderPrice, final int maxDiscountPrice,
            final CouponType type,
            final Integer discountAmount, final Double discountPercentage) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.type = type;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
    }

    public static CouponEntity from(final Coupon coupon) {
        CouponInfo couponInfo = coupon.getCouponInfo();
        return new CouponEntity(
                coupon.getId(),
                couponInfo.getName(),
                couponInfo.getMinOrderPrice(),
                couponInfo.getMaxDiscountPrice(),
                coupon.getType(),
                coupon.getDiscountAmount().orElse(null),
                coupon.getDiscountPercentage().orElse(null)
        );
    }

    public Coupon toDomain() {
        CouponInfo couponInfo = new CouponInfo(name, minOrderPrice, maxDiscountPrice);
        return Coupon.of(id, couponInfo, discountAmount, discountPercentage, type);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinOrderPrice() {
        return minOrderPrice;
    }

    public int getMaxDiscountPrice() {
        return maxDiscountPrice;
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
