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
        CouponType type = coupon.getType();
        Integer discountAmount = (type == CouponType.FIXED_AMOUNT) ? coupon.getValue() : null;
        Double discountPercent = (type == CouponType.FIXED_PERCENTAGE) ? (double) (coupon.getValue()) / 100 : null;
        return new CouponEntity(
                coupon.getId(),
                couponInfo.getName(),
                couponInfo.getMinOrderPrice(),
                couponInfo.getMaxDiscountPrice(),
                type,
                discountAmount,
                discountPercent
        );
    }

    public Coupon toDomain() {
        CouponInfo couponInfo = new CouponInfo(name, minOrderPrice, maxDiscountPrice);
        int value = (type == CouponType.FIXED_AMOUNT) ? discountAmount : (int) (discountPercentage * 100);
        return new Coupon(id, couponInfo, value, type);
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
