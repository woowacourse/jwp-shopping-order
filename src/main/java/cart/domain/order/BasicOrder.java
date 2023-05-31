package cart.domain.order;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.dto.MemberWithId;
import cart.domain.product.dto.ProductWithId;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;

public class BasicOrder implements Order {

    private final MemberWithId member;
    private final Integer totalPrice;
    private final Integer discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderDate;
    private final List<CartItemWithId> cartItems;

    public BasicOrder(final MemberWithId member, final Integer deliveryPrice,
                      final LocalDateTime orderDate, final List<CartItemWithId> cartItems) {
        this.member = member;
        this.orderDate = orderDate;
        this.cartItems = cartItems;
        this.totalPrice = calculateTotalOrderPrice();
        this.discountedTotalPrice = totalPrice;
        this.deliveryPrice = deliveryPrice;
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
    public CouponWithId getCoupon() {
        throw new BadRequestException(ErrorCode.NATUAL_ORDER_HAS_COUPON);
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

