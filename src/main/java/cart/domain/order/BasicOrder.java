package cart.domain.order;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.coupon.CouponWithId;
import cart.domain.member.MemberWithId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BasicOrder implements Order {

    private final MemberWithId member;
    private final BigDecimal totalPrice;
    private final BigDecimal discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderedAt;
    private final List<CartItemWithId> cartItems;
    private final Boolean isValid;

    public BasicOrder(final MemberWithId member, final Integer deliveryPrice,
                      final LocalDateTime orderedAt, final List<CartItemWithId> cartItems, final Boolean isValid) {
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
    public MemberWithId getMember() {
        return member;
    }

    @Override
    public Optional<CouponWithId> getCoupon() {
        return Optional.empty();
    }

    @Override
    public List<CartItemWithId> getCartItems() {
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
    public boolean isOwner(final String memberName) {
        return member.isSameName(memberName);
    }
}

