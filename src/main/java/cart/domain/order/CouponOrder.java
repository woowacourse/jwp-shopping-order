package cart.domain.order;

import cart.domain.cartitem.CartItem;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CouponOrder implements Order {

    private static final int PERCENTAGE = 100;
    private static final double DECIMAL_CONVERSION = 0.01;

    private final Long orderId;
    private final Member member;
    private final Coupon coupon;
    private final BigDecimal totalPrice;
    private final BigDecimal discountedTotalPrice;
    private final Integer deliveryPrice;
    private final LocalDateTime orderedAt;
    private final List<CartItem> cartItems;
    private final Boolean isValid;

    public CouponOrder(final Member member, final Coupon coupon, final Integer deliveryPrice,
                       final LocalDateTime orderedAt, final List<CartItem> cartItems, final Boolean isValid) {
        this(null, member, coupon, deliveryPrice, orderedAt, cartItems, isValid);
    }

    public CouponOrder(final Long orderId, final Member member, final Coupon coupon, final Integer deliveryPrice,
                       final LocalDateTime orderedAt, final List<CartItem> cartItems, final Boolean isValid) {
        this.orderId = orderId;
        this.member = member;
        this.coupon = coupon;
        this.orderedAt = orderedAt;
        this.cartItems = cartItems;
        this.totalPrice = calculateTotalOrderPrice();
        this.discountedTotalPrice = calculateDiscountPrice();
        this.deliveryPrice = deliveryPrice;
        this.isValid = isValid;
    }

    private BigDecimal calculateTotalOrderPrice() {
        return OrderPriceCalculator.calculateTotalOrderPrice(cartItems);
    }

    private BigDecimal calculateDiscountPrice() {
        final int discountRate = coupon.discountRate();
        final BigDecimal convertedDiscountRate = BigDecimalConverter.convert(
            (PERCENTAGE - discountRate) * DECIMAL_CONVERSION);
        return totalPrice.multiply(convertedDiscountRate).setScale(0, RoundingMode.DOWN);
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
        return Optional.of(coupon);
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

