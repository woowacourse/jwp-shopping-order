package cart.coupon.domain;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final CouponType couponType;
    private final Long memberId;

    public Coupon(String name, DiscountPolicy discountPolicy, CouponType couponType, Long memberId) {
        this(null, name, discountPolicy, couponType, memberId);
    }

    public Coupon(Long id, String name, DiscountPolicy discountPolicy, CouponType couponType, Long memberId) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.couponType = couponType;
        this.memberId = memberId;
    }

    public int apply(int price) {
        return discountPolicy.calculatePrice(price);
    }

    public boolean canApply(Long productId) {
        return couponType.canApply(productId);
    }

    public int value() {
        return discountPolicy.getValue();
    }

    public TargetType targetType() {
        return couponType.getTargetType();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public Long getMemberId() {
        return memberId;
    }
}
