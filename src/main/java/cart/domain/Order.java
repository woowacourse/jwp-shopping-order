package cart.domain;

import cart.domain.common.Money;
import cart.domain.coupon.Coupon;
import java.util.List;

public class Order {

    private final Long id;
    private final Coupon coupon;
    private final Long memberId;
    private final Money deliveryFee;
    private final List<CartItem> cartItems;

    public Order(final Coupon coupon, final Long memberId, final List<CartItem> cartItems) {
        this(null, coupon, memberId, Money.from(3000L), cartItems);
    }

    public Order(
            final Long id,
            final Coupon coupon,
            final Long memberId,
            final Money deliveryFee,
            final List<CartItem> cartItems
    ) {
        this.id = id;
        this.coupon = coupon;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
        this.cartItems = cartItems;
    }

    public Money calculateDiscountPrice() {
        final Money totalPrice = calculateTotalPrice();
        final Money subtrahend = coupon.calculatePrice(totalPrice);
        return totalPrice.minus(subtrahend);
    }

    public Money calculateTotalPrice() {
        return cartItems.stream()
                .map(CartItem::calculateTotalPrice)
                .reduce(Money.ZERO, Money::plus);
    }

    public Money calculateDeliveryFee() {
        return coupon.calculateDeliveryFee(calculateTotalPrice(), deliveryFee);
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
