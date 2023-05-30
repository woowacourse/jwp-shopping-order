package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderProducts {
    private static final double POINT_RATE = 0.05;

    private final List<OrderProduct> products;

    public OrderProducts(final List<CartItem> cartItems) {
        products = cartItems.stream()
                .map(cartItem -> new OrderProduct(
                                cartItem.getProduct(),
                                cartItem.getProduct().getPrice(),
                                cartItem.getQuantity()
                        )
                ).collect(Collectors.toList());
    }

    public Integer getTotalAmount() {
        return products.stream()
                .mapToInt(product -> product.getPurchasedPrice() * product.getQuantity())
                .sum();
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public Integer getSavedPoint() {
        return (int) (getTotalAmount() * POINT_RATE);
    }
}
