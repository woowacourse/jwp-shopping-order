package cart.cart.presentation.dto;

import cart.cart.Cart;

import java.util.List;
import java.util.stream.Collectors;

public class DiscountResponse {
    private List<CartItemDiscountResponse> cartItemsPrice;
    private DeliveryDiscountResponse deliveryPrice;
    private DiscountFromTotalPrice discountFromTotalPrice;

    public DiscountResponse() {
    }

    public DiscountResponse(List<CartItemDiscountResponse> cartItemsPrice, DeliveryDiscountResponse deliveryPrice, DiscountFromTotalPrice discountFromTotalPrice) {
        this.cartItemsPrice = cartItemsPrice;
        this.deliveryPrice = deliveryPrice;
        this.discountFromTotalPrice = discountFromTotalPrice;
    }

    public static DiscountResponse from(Cart cart, int discountPriceFromTotalPrice) {
        final var cartItemDiscountResponses = cart.getCartItems()
                .stream().map(CartItemDiscountResponse::from)
                .collect(Collectors.toList());
        final var deliveryDiscountResponse = DeliveryDiscountResponse.from(cart.getDeliveryPrice().getPrice());
        final var discountFromTotalPrice = new DiscountFromTotalPrice(discountPriceFromTotalPrice);
        return new DiscountResponse(cartItemDiscountResponses, deliveryDiscountResponse, discountFromTotalPrice);
    }

    public List<CartItemDiscountResponse> getCartItemsPrice() {
        return cartItemsPrice;
    }

    public DeliveryDiscountResponse getDeliveryPrice() {
        return deliveryPrice;
    }

    public DiscountFromTotalPrice getDiscountFromTotalPrice() {
        return discountFromTotalPrice;
    }
}
