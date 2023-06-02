package cart.domain.coupon;

import cart.domain.Model;
import cart.domain.Order;

import java.util.Objects;

public abstract class Coupon implements Model, DiscountAction, CouponUseConditionAction {
    private final Long id;
    private final String name;
    private final DiscountType discountType;
    private final Double discountPercent;
    private final Integer discountAmount;
    private final Integer minimumPrice;


    protected Coupon(Long id, String name, DiscountType discountType, Double discountPercent, Integer discountAmount, Integer minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    protected Coupon(Long id, String name, DiscountType discountType, Double discountPercent, Integer discountAmount) {
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

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
