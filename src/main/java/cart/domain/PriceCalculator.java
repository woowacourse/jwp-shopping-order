package cart.domain;

import java.util.List;

public class PriceCalculator {
    public static Money calculate(List<CartItem> cartItems) {
        return new Money(cartItems.stream()
            .mapToInt(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
            .sum()
        );
    }
}
