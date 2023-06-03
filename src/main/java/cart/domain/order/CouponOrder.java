package cart.domain.order;

import cart.domain.cartitem.dto.CartItemWithId;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.dto.MemberWithId;
import cart.domain.product.dto.ProductWithId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CouponOrder implements Order {

    private static final int PERCENTAGE = 100;
    private static final double DECIMAL_CONVERSION = 0.01;

    private final MemberWithId member;
    private final CouponWithId coupon;
    private final Integer totalPrice;
    private final Integer discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderedAt;
    private final List<CartItemWithId> cartItems;
    private final Boolean isValid;

    public CouponOrder(final MemberWithId member, final CouponWithId coupon, final Integer deliveryPrice,
                       final LocalDateTime orderedAt, final List<CartItemWithId> cartItems, final Boolean isValid) {
        this.member = member;
        this.coupon = coupon;
        this.orderedAt = orderedAt;
        this.cartItems = cartItems;
        this.totalPrice = calculateTotalPrice();
        this.discountedTotalPrice = calculateDiscountPrice();
        this.deliveryPrice = deliveryPrice;
        this.isValid = isValid;
    }

    private int calculateDiscountPrice() {
        final int discountRate = coupon.getCoupon().discountRate();
        return (int) Math.floor(totalPrice * (PERCENTAGE - discountRate) * DECIMAL_CONVERSION);
    }

    private int calculateTotalPrice() {
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
        return Optional.of(coupon);
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

