package cart.domain;

import java.util.List;

public class PriceCalculator {
    public static Money calculate(List<CartItem> cartItems) {
        return new Money(cartItems.stream()
            .mapToInt(CartItem::getTotalPrice)
            .sum()
        );
    }
}
