package cart.domain.coupon;

public class CouponInfo {

    private final long id;
    private final String name;
    private final int minOrderPrice;
    private final int maxDiscountPrice;

    public CouponInfo(final long id, final String name, final int minOrderPrice, final int maxDiscountPrice) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
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

    public int getMaxDiscountPrice() {
        return maxDiscountPrice;
    }
}
