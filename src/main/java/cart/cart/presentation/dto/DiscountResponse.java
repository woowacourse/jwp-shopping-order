package cart.cart.presentation.dto;

import cart.cart.Cart;

import java.util.List;
import java.util.stream.Collectors;

public class DiscountResponse {
    private List<CartItemDiscountResponse> products;
    private DeliveryDiscountResponse deliveryPrice;

    public DiscountResponse() {
    }

    public DiscountResponse(List<CartItemDiscountResponse> products, DeliveryDiscountResponse deliveryPrice) {
        this.products = products;
        this.deliveryPrice = deliveryPrice;
    }

    public static DiscountResponse from(Cart cart) {
        final var cartItemDiscountResponses = cart.getCartItems()
                .stream().map(CartItemDiscountResponse::from)
                .collect(Collectors.toList());
        final var deliveryDiscountResponse = DeliveryDiscountResponse.from(cart.getDeliveryPrice());
        return new DiscountResponse(cartItemDiscountResponses, deliveryDiscountResponse);
    }

    public List<CartItemDiscountResponse> getProducts() {
        return products;
    }

    public DeliveryDiscountResponse getDeliveryPrice() {
        return deliveryPrice;
    }
}
