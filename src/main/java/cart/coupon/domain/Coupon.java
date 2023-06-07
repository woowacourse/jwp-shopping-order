package cart.coupon.domain;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final CouponStrategy couponStrategy;
    private final Long memberId;

    public Coupon(String name, DiscountPolicy discountPolicy, CouponStrategy couponStrategy, Long memberId) {
        this(null, name, discountPolicy, couponStrategy, memberId);
    }

    public Coupon(Long id, String name, DiscountPolicy discountPolicy, CouponStrategy couponStrategy, Long memberId) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.couponStrategy = couponStrategy;
        this.memberId = memberId;
    }

    public int apply(int price) {
        return discountPolicy.calculatePrice(price);
    }

    public boolean canApply(Long productId) {
        return couponStrategy.canApply(productId);
    }

    public int value() {
        return discountPolicy.getValue();
    }

    public DiscountType discountType() {
        return discountPolicy.getDiscountType();
    }

    public TargetType targetType() {
        return couponStrategy.getTargetType();
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

    public CouponStrategy getCouponType() {
        return couponStrategy;
    }

    public Long getMemberId() {
        return memberId;
    }
}
