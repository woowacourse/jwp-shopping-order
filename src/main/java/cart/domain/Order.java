package cart.domain;

import cart.domain.coupon.Coupon;

import java.util.List;
import java.util.Objects;

public class Order {
    private final Long id;
    private final Integer originalPrice;
    private final Integer discountPrice;
    private final List<OrderProduct> orderProducts;
    private final Coupon usedCoupon;
    private Boolean confirmState;
    private final Member member;

    public Order(Long id, Integer originalPrice, Integer discountPrice, List<OrderProduct> orderProducts, Coupon usedCoupon, Boolean confirmState, Member member) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.orderProducts = orderProducts;
        this.usedCoupon = usedCoupon;
        this.confirmState = confirmState;
        this.member = member;
    }

    public Order(Integer originalPrice, Integer discountPrice, List<OrderProduct> orderProducts, Coupon usedCoupon, Member member) {
        this(null, originalPrice, discountPrice, orderProducts, usedCoupon, false, member);
    }

    public Order(Integer originalPrice, Integer discountPrice, List<OrderProduct> orderProducts, Member member) {
        this(null, originalPrice, discountPrice, orderProducts, null, false, member);
    }

    public Long getId() {
        return id;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public Coupon getUsedCoupon() {
        return usedCoupon;
    }

    public Boolean getConfirmState() {
        return confirmState;
    }

    public Member getMember() {
        return member;
    }

    public void confirm() {
        this.confirmState = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
