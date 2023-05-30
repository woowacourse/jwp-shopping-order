package cart.domain.coupon;

import cart.domain.Order;

public abstract class Coupon implements DiscountAction, CouponUseConditionAction {
    private final Long id;
    private final String name;
    private final DiscountType discountType;
    private final Integer discountPercent;
    private final Integer discountAmount;
    private final Integer minimumPrice;


    protected Coupon(Long id, String name, DiscountType discountType, Integer discountPercent, Integer discountAmount, Integer minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    protected Coupon(Long id, String name, DiscountType discountType, Integer discountPercent, Integer discountAmount) {
        this(id, name, discountType, discountPercent, discountAmount, 0);
    }

    @Override
    public boolean isUsable(Order order) {
        return order.getOriginalPrice() >= minimumPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }
}
