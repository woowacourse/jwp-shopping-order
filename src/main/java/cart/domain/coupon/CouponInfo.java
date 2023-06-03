package cart.domain.coupon;

public class CouponInfo {

    private final String name;
    private final int minOrderPrice;
    private final int maxDiscountPrice;

    public CouponInfo(final String name, final int minOrderPrice, final int maxDiscountPrice) {
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
    }

    public static CouponInfo none() {
        return new CouponInfo("", 0, 0);
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
}
