package cart.domain;

public class Coupon {
    private Long id;
    private String name;
    private CouponType couponType;
    private float discountRate;
    private int discountAmount;
    private int minimumPrice;

    public Coupon(Long id, String name, CouponType couponType, float discountRate, int discountAmount, int minimumPrice) {
        this.id = id;
        this.name = name;
        this.couponType = couponType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CouponType getCouponType() {
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
