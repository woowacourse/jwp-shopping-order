package cart.cart.presentation.dto;

import cart.cart.Cart;
import cart.cart.domain.cartitem.CartItem;
import cart.coupon.Coupon;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {
    private List<CartItemResponse> products;
    private int deliveryPrice;
    private List<CouponResponse> coupons;

    private CartResponse(List<CartItemResponse> cartItemResponses, int deliveryPrice, List<CouponResponse> couponResponses) {
        this.products = cartItemResponses;
        this.deliveryPrice = deliveryPrice;
        this.coupons = couponResponses;
    }

    public static CartResponse from(Cart cart) {
        final var cartItemResponses = cart.getCartItems()
                .stream().map(CartItemResponse::from)
                .collect(Collectors.toList());
        final var couponResponses = cart.getCoupons()
                .stream().map(CouponResponse::from)
                .collect(Collectors.toList());

        return new CartResponse(cartItemResponses, cart.getDeliveryPrice(), couponResponses);
    }

    public List<CartItemResponse> getProducts() {
        return products;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
