package cart.domain;

public class PercentageCoupon implements Coupon{
    private final CouponInfo couponInfo;
    private final Double discountPercentage;

    public PercentageCoupon(final CouponInfo couponInfo, final Double discountPercentage) {
        this.couponInfo = couponInfo;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public boolean isAvailable(final Integer totalPrice) {
        return couponInfo.getMinPrice() <= totalPrice;
    }

    @Override
    public Integer calculateDiscount(final Integer totalPrice) {
        if (couponInfo.getMinPrice() > totalPrice) {
            throw new IllegalArgumentException("주문 금액이 최소 주문 금액보다 작습니다.");
        }
        double discountPrice = totalPrice * discountPercentage;
        if (discountPrice > couponInfo.getMaxPrice()) {
            return totalPrice - couponInfo.getMaxPrice();
        }
        // TODO: 5/29/23 이거 반올림? 
        return totalPrice - (int)discountPrice;
    }
}
