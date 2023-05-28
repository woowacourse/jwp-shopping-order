package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.product.Product;
import cart.dto.product.ProductUsingCouponAndSaleResponse;

import java.util.List;
import java.util.stream.Collectors;

public class Cart {

    private static final int DEFAULT_DELIVERY_FEE = 3000;

    private Long id;
    private final CartItems cartItems;
    private int deliveryFee;

    public Cart(final Long id, final CartItems cartItems) {
        this.id = id;
        this.cartItems = cartItems;
        this.deliveryFee = DEFAULT_DELIVERY_FEE;
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
        return cartItems.getTotalPriceWithoutCoupons();
    }

    public int calculateItemsUsingCoupons(final List<Coupon> reqCoupons) {
        int price = cartItems.getTotalPriceUsingCoupons();

        for (Coupon reqCoupon : reqCoupons) {
            price = reqCoupon.calculate(price);
        }

        return price;
    }

    public int calculateDeliveryFeeUsingCoupons(final List<Coupon> reqCoupons) {
        int price = deliveryFee;

        List<Coupon> coupons = reqCoupons.stream()
                .filter(Coupon::isDeliveryCoupon)
                .collect(Collectors.toList());

        for (Coupon coupon : coupons) {
            price = coupon.calculate(price);
        }

        return price;
    }

    public List<ProductUsingCouponAndSaleResponse> getProductUsingCouponAndSaleResponse(final List<Coupon> requestCoupons) {
        return cartItems.getProductUsingCouponAndSaleResponse(requestCoupons);
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems.getCartItems();
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
