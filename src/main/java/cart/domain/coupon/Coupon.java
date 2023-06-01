package cart.domain.coupon;

import cart.exception.CouponException;

import java.util.Objects;

public class Coupon {
    private static final int MINIMUM_NAME_LENGTH = 1;
    private final Long id;
    private final String name;
    private final CouponTypes couponTypes;
    private final int minimumPrice;
    private final int discountPrice;
    private final double discountRate;

    public static Coupon empty(){
        return new Coupon(
                null,
                "EMPTY_COUPON",
                new EmptyDiscount()
                ,0,0,0
        );
    }

    public static Coupon bonusCoupon(){
        return new Coupon(
                null,
                "주문확정_보너스쿠폰",
                new PriceDiscount(),
                5000,1000,0
        );
    }

    public Coupon(String name, CouponTypes couponTypes, int minimumPrice, int discountPrice, double discountRate) {
        this(null,name,couponTypes,minimumPrice,discountPrice,discountRate);
    }

    public Coupon(Long id, String name, CouponTypes couponTypes, int minimumPrice, int discountPrice, double discountRate) {
        validate(name);
        this.id = id;
        this.name = name;
        this.couponTypes = couponTypes;
        this.minimumPrice = minimumPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
    }

    private void validate(String name) {
        if(name.length() < MINIMUM_NAME_LENGTH){
            throw new CouponException("쿠폰의 이름은 최소 1글자 이상입니다.");
        }
    }

    public int applyCouponPrice(int totalPrice){
        return couponTypes.calculatePrice(totalPrice,minimumPrice,discountPrice,discountRate);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CouponTypes getCouponTypes() {
        return couponTypes;
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id) && Objects.equals(name, coupon.name) && Objects.equals(couponTypes, coupon.couponTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, couponTypes);
    }
}
