package cart.dto;

import cart.domain.Coupon;

import java.util.Optional;

public class CouponResponse {
    private long id;
    private String name;
    private String discountType;
    private float discountRate;
    private int discountAmount;
    private int minimumPrice;
    private boolean issuable;

    private CouponResponse() {

    }

    private CouponResponse(long id, String name, String discountType, float discountRate, int discountAmount, int minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    private CouponResponse(long id, String name, String discountType, float discountRate, int discountAmount, boolean issuable) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.issuable = issuable;
    }

    // TODO : null 값 고민해보기
    public static CouponResponse of(Optional<Coupon> coupon) {
        if (coupon.isEmpty()) {
            return new CouponResponse();
        }
        return new CouponResponse(
                coupon.get().getId(),
                coupon.get().getName(),
                coupon.get().getCouponType().value(),
                coupon.get().getDiscountRate(),
                coupon.get().getDiscountAmount(),
                coupon.get().getMinimumPrice()
        );
    }

    public static CouponResponse issuableOf(Coupon coupon, boolean issuable) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getCouponType().value(),
                coupon.getDiscountRate(),
                coupon.getDiscountAmount(),
                issuable
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public boolean getIssuable() {
        return issuable;
    }
}
