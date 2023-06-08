package cart.domain;

import cart.domain.coupon.Coupon;

import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final CartItems cartProducts;
    private final boolean confirmState;
    private final Coupon coupon;

    public Order(Member member, CartItems cartProducts, Coupon coupon) {
        this(null, member, cartProducts, false, coupon);
    }

    public Order(Long id, Member member, CartItems cartProducts, boolean confirmState, Coupon coupon) {
        this.id = id;
        this.member = member;
        this.cartProducts = cartProducts;
        this.confirmState = confirmState;
        this.coupon = coupon;
    }

    public int calculatePrice() {
        return cartProducts.calculatePrice();
    }

    public int calculateDiscountPrice() {
        int totalPrice = cartProducts.calculatePrice();
        return coupon.applyCouponPrice(totalPrice);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getCartProducts() {
        return cartProducts.getCartProducts();
    }

    public Boolean getConfirmState() {
        return confirmState;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
