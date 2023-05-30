package cart.dao.entity;

public class CouponEntity {
    private final long id;
    private final String name;
    private final String couponType;
    private final float discountRate;
    private final int discountAmount;
    private final int minimumPrice;

    public CouponEntity(long id, String name, String couponType, float discountRate, int discountAmount, int minimumPrice) {
        this.id = id;
        this.name = name;
        this.couponType = couponType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCouponType() {
        return couponType;
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
}
