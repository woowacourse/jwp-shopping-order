package cart.cart.presentation.dto;

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

    public static CartResponse from(List<CartItem> products, int deliveryPrice, List<Coupon> coupons) {
        final var cartItemResponses = products.stream().map(cartItem ->
                        CartItemResponse.from(cartItem.getId(),
                                cartItem.getProduct().getName(),
                                cartItem.getProduct().getPrice(),
                                cartItem.getProduct().getImageUrl(),
                                cartItem.getDiscountPrice()))
                .collect(Collectors.toList());
        final var couponResponses = coupons.stream()
                .map(coupon -> CouponResponse.from(coupon.getId(), coupon.getName()))
                .collect(Collectors.toList());
        return new CartResponse(cartItemResponses, deliveryPrice, couponResponses);
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
