package cart.domain.order;

import cart.domain.cartitem.CartItem;
import cart.domain.product.Product;
import java.math.BigDecimal;
import java.util.List;

public class OrderPriceCalculator {

    static BigDecimal calculateTotalOrderPrice(final List<CartItem> cartItems) {
        return cartItems.stream()
            .map(cartItem -> {
                final Product product = cartItem.getProduct();
                final int productPrice = product.getPrice();
                final int productQuantity = cartItem.getQuantity();
                return BigDecimalConverter.convert(productPrice)
                    .multiply(BigDecimalConverter.convert(productQuantity));
            }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
