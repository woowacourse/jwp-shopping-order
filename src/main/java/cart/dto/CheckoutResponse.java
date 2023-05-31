package cart.dto;

import cart.domain.OrderCheckout;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutResponse {
    private final List<CartItemResponse> cartItems;
    private final int totalPrice;
    private final int currentPoints;
    private final int earnedPoints;
    private final int availablePoints;

    private CheckoutResponse(final List<CartItemResponse> cartItems, final int totalPrice, final int currentPoints, final int earnedPoints, final int availablePoints) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.currentPoints = currentPoints;
        this.earnedPoints = earnedPoints;
        this.availablePoints = availablePoints;
    }

    public static CheckoutResponse of(final OrderCheckout orderCheckout) {
        final List<CartItemResponse> cartItemResponses = orderCheckout.getCartItems().stream().map(CartItemResponse::of)
                .collect(Collectors.toList());
        return new CheckoutResponse(
                cartItemResponses,
                orderCheckout.getTotalPrice(),
                orderCheckout.getCurrentPoints(),
                orderCheckout.getEarnedPoints(),
                orderCheckout.getAvailablePoints()
        );
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }
}
