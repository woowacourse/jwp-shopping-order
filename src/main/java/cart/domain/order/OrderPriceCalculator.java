package cart.domain.order;

import cart.domain.cartitem.CartItem;
import cart.domain.product.Product;
import java.math.BigDecimal;
import java.util.List;

public class OrderPriceCalculator {

    static BigDecimal calculateTotalOrderPrice(final List<CartItem> cartItems) {
        return cartItems.stream()
            .map(OrderPriceCalculator::calculatePrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculatePrice(final CartItem cartItem) {
        final Product product = cartItem.getProduct();
        final int productPrice = product.getPrice();
        final int productQuantity = cartItem.getQuantity();
        return BigDecimal.valueOf(productPrice)
            .multiply(BigDecimal.valueOf(productQuantity));
    }
}
