package cart.domain.order;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.dto.MemberWithId;
import cart.domain.product.dto.ProductWithId;
import java.time.LocalDateTime;
import java.util.List;

public class CouponOrder implements Order {

    private static final int PERCENTAGE = 100;
    private static final double DECIMAL_CONVERSION = 0.01;

    private final MemberWithId member;
    private final CouponWithId coupon;
    private final Integer totalPrice;
    private final Integer discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderDate;
    private final List<CartItemWithId> cartItems;

    public CouponOrder(final MemberWithId member, final CouponWithId coupon, final Integer deliveryPrice,
                       final LocalDateTime orderDate, final List<CartItemWithId> cartItems) {
        this.member = member;
        this.coupon = coupon;
        this.orderDate = orderDate;
        this.cartItems = cartItems;
        this.totalPrice = calculateTotalPrice();
        this.discountedTotalPrice = calculateDiscountPrice();
        this.deliveryPrice = deliveryPrice;
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
    public CouponWithId getCoupon() {
        return coupon;
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
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    @Override
    public Integer getTotalPrice() {
        return totalPrice;
    }

    @Override
    public Integer getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }
}

