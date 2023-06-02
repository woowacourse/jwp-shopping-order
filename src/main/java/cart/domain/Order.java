package cart.domain;

import cart.domain.coupon.Coupon;
import cart.exception.OrderException;

import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final List<CartItem> cartProducts;
    private final boolean confirmState;
    private final Coupon coupon;

    public Order(Member member, List<CartItem> cartProducts, Coupon coupon) {
        this(null, member, cartProducts, false, coupon);
    }

    public Order(Long id, Member member, List<CartItem> cartProducts, boolean confirmState, Coupon coupon) {
        validate(cartProducts);
        this.id = id;
        this.member = member;
        this.cartProducts = cartProducts;
        this.confirmState = confirmState;
        this.coupon = coupon;
    }

    private void validate(List<CartItem> cartProducts) {
        if (cartProducts.isEmpty()) {
            throw new OrderException("주문 상품이 비어있습니다.");
        }
    }

    public int calculatePrice() {
        return cartProducts.stream()
                .mapToInt(it -> it.getProduct().getPrice() * it.getQuantity()).sum();
    }

    public int calculateDiscountPrice() {
        int totalPrice = cartProducts.stream()
                .mapToInt(it -> it.getProduct().getPrice() * it.getQuantity()).sum();
        return coupon.applyCouponPrice(totalPrice);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getCartProducts() {
        return cartProducts;
    }

    public Boolean getConfirmState() {
        return confirmState;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
