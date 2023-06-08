package cart.application.dto.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponInfo;
import cart.domain.coupon.CouponType;

public class FindCouponResponse {

    private long id;
    private String name;
    private int minOrderPrice;
    private Integer maxDiscountPrice;
    private CouponType type;
    private Integer discountAmount;
    private Double discountPercentage;

    private FindCouponResponse() {
    }

    public FindCouponResponse(final long id, final String name, final int minOrderPrice, final Integer maxDiscountPrice,
            final CouponType type, final Integer discountAmount, final Double discountPercentage) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.type = type;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
    }

    public static FindCouponResponse from(final Coupon coupon) {
        CouponInfo couponInfo = coupon.getCouponInfo();
        return new FindCouponResponse(
                coupon.getId(),
                couponInfo.getName(),
                couponInfo.getMinOrderPrice(),
                couponInfo.getMaxDiscountPrice(),
                coupon.getType(),
                coupon.getDiscountAmount().orElse(null),
                coupon.getDiscountPercentage().orElse(null));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinOrderPrice() {
        return minOrderPrice;
    }

    public Integer getMaxDiscountPrice() {
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
