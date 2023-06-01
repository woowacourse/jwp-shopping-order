package cart.domain;

import java.util.List;

public class Order {
    private final Member member;
    private final List<CartItem> cartItems;
    private final Coupon coupon;
    private final Money totalPrice;
    private Long id;

    public Order(Member member, List<CartItem> cartItems, Coupon coupon) {
        this.member = member;
        this.cartItems = cartItems;
        this.coupon = coupon;
        this.totalPrice = PriceCalculator.calculate(cartItems);
    }

    public Order(Long id, Member member, List<CartItem> cartItems, Coupon coupon) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.coupon = coupon;
        this.totalPrice = PriceCalculator.calculate(cartItems);
    }

    public Money getDiscountedPrice() {
        return coupon.discount(totalPrice);
    }

    public Money getShippingFee() {
        return ShippingFeePolicy.findShippingFee(totalPrice);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
