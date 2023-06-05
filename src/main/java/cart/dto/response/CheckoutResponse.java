package cart.dto.response;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutResponse {

    private List<CartItemResponse> cartItems;
    private int totalPrice;
    private int currentPoints;
    private int earnedPoints;
    private int availablePoints;

    private CheckoutResponse() {
    }

    private CheckoutResponse(
            List<CartItemResponse> cartItems,
            int totalPrice,
            int currentPoints,
            int earnedPoints,
            int availablePoints
    ) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.currentPoints = currentPoints;
        this.earnedPoints = earnedPoints;
        this.availablePoints = availablePoints;
    }

    public static CheckoutResponse of(
            List<CartItem> checkedCartItems,
            Member member,
            Money totalPrice,
            Money earnedPoints,
            Money availablePoints
    ) {
        List<CartItemResponse> cartItemResponses = checkedCartItems
                .stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());

        return new CheckoutResponse(
                cartItemResponses,
                totalPrice.getValue(),
                member.getPoint(),
                earnedPoints.getValue(),
                availablePoints.getValue()
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
