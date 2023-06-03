package cart.domain.order;

import cart.domain.cartitem.dto.CartItemWithId;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.dto.MemberWithId;
import cart.domain.product.dto.ProductWithId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BasicOrder implements Order {

    private final MemberWithId member;
    private final Integer totalPrice;
    private final Integer discountedTotalPrice;
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

    private int calculateTotalOrderPrice() {
        return cartItems.stream()
            .mapToInt(cartItemWithId -> {
                final ProductWithId productWithId = cartItemWithId.getProduct();
                final int productPrice = productWithId.getProduct().getPrice();
                final int productQuantity = cartItemWithId.getQuantity();
                return productPrice * productQuantity;
            }).sum();
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
    public Integer getTotalPrice() {
        return totalPrice;
    }

    @Override
    public Integer getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    @Override
    public Boolean isValid() {
        return isValid;
    }
}

