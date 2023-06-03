package cart.controller.cart.dto.dto;

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

    public static DiscountResponse from(Cart cart) {
        final var cartItemDiscountResponses = getCartItemDiscountResponses(cart);
        final var deliveryDiscountResponse = getDeliveryDiscountResponse(cart);
        final var discountFromTotalPrice = getDiscountFromTotalPrice(cart);

        return new DiscountResponse(cartItemDiscountResponses, deliveryDiscountResponse, discountFromTotalPrice);
    }

    private static List<CartItemDiscountResponse> getCartItemDiscountResponses(Cart cart) {
        return cart.getCartItems()
                .stream().map(CartItemDiscountResponse::from)
                .collect(Collectors.toList());
    }

    private static DeliveryDiscountResponse getDeliveryDiscountResponse(Cart cart) {
        return new DeliveryDiscountResponse(cart.getOriginalDeliveryPrice(), cart.getDiscountDeliveryPrice());
    }

    private static DiscountFromTotalPrice getDiscountFromTotalPrice(Cart cart) {
        return new DiscountFromTotalPrice(cart.getDiscountFromTotalPrice());
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
