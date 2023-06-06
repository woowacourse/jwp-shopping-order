package cart.domain.order;

import cart.domain.cartitem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BasicOrder implements Order {

    private final Long orderId;
    private final Member member;
    private final BigDecimal totalPrice;
    private final BigDecimal discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderedAt;
    private final List<CartItem> cartItems;
    private final Boolean isValid;

    public BasicOrder(final Member member, final Integer deliveryPrice, final LocalDateTime orderedAt,
                      final List<CartItem> cartItems, final Boolean isValid) {
        this(null, member, deliveryPrice, orderedAt, cartItems, isValid);
    }

    public BasicOrder(final Long orderId, final Member member, final Integer deliveryPrice,
                      final LocalDateTime orderedAt, final List<CartItem> cartItems, final Boolean isValid) {
        this.orderId = orderId;
        this.member = member;
        this.orderedAt = orderedAt;
        this.cartItems = cartItems;
        this.totalPrice = calculateTotalOrderPrice();
        this.discountedTotalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
        this.isValid = isValid;
    }

    private BigDecimal calculateTotalOrderPrice() {
        return OrderPriceCalculator.calculateTotalOrderPrice(cartItems);
    }

    @Override
    public Long getOrderId() {
        return orderId;
    }

    @Override
    public Member getMember() {
        return member;
    }

    @Override
    public Optional<Coupon> getCoupon() {
        return Optional.empty();
    }

    @Override
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    @Override
    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Override
    public BigDecimal getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    @Override
    public Boolean isValid() {
        return isValid;
    }

    @Override
    public boolean isNotOwner(final String memberName) {
        return !member.isSameName(memberName);
    }
}

