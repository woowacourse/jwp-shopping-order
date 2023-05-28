package cart.domain.cart;

import cart.domain.coupon.Coupon;
import cart.domain.product.Product;
import cart.dto.product.ProductUsingCouponAndSaleResponse;

import java.util.List;
import java.util.stream.Collectors;

public class Cart {

    private Long id;
    private final CartItems cartItems;
    private DeliveryFee deliveryFee;

    public Cart(final Long id, final CartItems cartItems) {
        this.id = id;
        this.cartItems = cartItems;
        this.deliveryFee = DeliveryFee.createDefault();
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
        int price = deliveryFee.getFee();

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

    public List<String> getProductNames() {
        return cartItems.getCartItems().stream()
                .map(cartItem -> cartItem.getProduct().getName())
                .collect(Collectors.toList());
    }

    public int getDeliveryFee() {
        return deliveryFee.getFee();
    }
}
