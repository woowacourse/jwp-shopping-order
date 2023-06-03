package cart.dto.response;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.PointDiscountPolicy;
import cart.domain.PointEarnPolicy;
import java.util.ArrayList;
import java.util.List;

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
            PointDiscountPolicy pointDiscountPolicy,
            PointEarnPolicy pointEarnPolicy
    ) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        Money totalPrice = new Money(0);

        for (CartItem cartItem : checkedCartItems) {
            cartItemResponses.add(CartItemResponse.of(cartItem));
            totalPrice = totalPrice.add(cartItem.getProduct().price());
        }

        Money currentPoints = member.point();
        Money earnedPoints = pointEarnPolicy.calculateEarnPoints(totalPrice);
        Money moneyCondition = pointDiscountPolicy.calculatePointCondition(totalPrice);
        Money availablePoints = calculateAvailablePoints(currentPoints, moneyCondition);

        return new CheckoutResponse(cartItemResponses, totalPrice.getValue(), currentPoints.getValue(),
                earnedPoints.getValue(), availablePoints.getValue());
    }

    private static Money calculateAvailablePoints(Money currentPoints, Money moneyCondition) {
        if (currentPoints.isGreaterThan(moneyCondition)) {
            return moneyCondition;
        }
        return currentPoints;
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
