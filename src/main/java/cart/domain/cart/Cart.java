package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;

public class Cart {

    private Long id;
    private final Member member;
    private final CartItems cartItems;
    private int deliveryFee;

    public Cart(final Long id, final Member member, final CartItems cartItems) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.deliveryFee = 3000;
    }

    public Cart(final Member member, final CartItems cartItems) {
        this.member = member;
        this.cartItems = cartItems;
    }

    public CartItem addItem(final Product product) {
        return cartItems.add(product);
    }

    public void removeItem(final CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public void changeQuantity(final long cartItemId, final int quantity) {
        cartItems.changeQuantity(cartItemId, quantity);
    }

    public boolean hasItem(final CartItem cartItem) {
        return cartItems.hasItem(cartItem);
    }


    // TODO : 유저가 쿠폰을 가지고 있는지 확인하기
    public int calculateOriginPrice() {
        return cartItems.getTotalPrice();
    }

    public int calculateItems(final List<Coupon> reqCoupons) {
        int price = cartItems.getTotalPrice();

        for (Coupon reqCoupon : reqCoupons) {
            price = reqCoupon.calculate(price);
        }

        return price;
    }

    public int calculateDeliveryFee(final List<Coupon> reqCoupons) {
        int price = deliveryFee;

        List<Coupon> collect = reqCoupons.stream()
                .filter(Coupon::isDeliveryCoupon)
                .collect(Collectors.toList());

        for (Coupon coupon : collect) {
            price = coupon.calculate(price);
        }

        return price;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getCartItems() {
        return cartItems.getCartItems();
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
