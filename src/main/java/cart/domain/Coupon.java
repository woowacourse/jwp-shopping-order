package cart.domain;

public class Coupon {

    private Long id;
    private String name;
    private CouponType type;
    private int discountAmount;

    public Coupon(String name, CouponType type, int discountAmount) {
        this(null, name, type, discountAmount);
    }

    public Coupon(Long id, String name, CouponType type, int discountAmount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountAmount = discountAmount;
    }

    public int apply(int paymentAmount) {
        int afterDiscountPayment = type.apply(paymentAmount, discountAmount);
        if (afterDiscountPayment <= 0) {
            return 0;
        }
        return afterDiscountPayment;
    }
}
