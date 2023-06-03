package cart.domain.order;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.product.ProductWithId;
import java.math.BigDecimal;
import java.util.List;

public class OrderPriceCalculator {

    static BigDecimal calculateTotalOrderPrice(final List<CartItemWithId> cartItems) {
        return cartItems.stream()
            .map(cartItemWithId -> {
                final ProductWithId product = cartItemWithId.getProduct();
                final int productPrice = product.getProduct().getPrice();
                final int productQuantity = cartItemWithId.getQuantity();
                return BigDecimalConverter.convert(productPrice)
                    .multiply(BigDecimalConverter.convert(productQuantity));
            }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
