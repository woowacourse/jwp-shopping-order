package cart.domain;

import java.util.List;

public class OrderProducts {
    private static final double POINT_RATE = 0.05;
    private final List<CartItem> products;

    public OrderProducts(final List<CartItem> products) {
        this.products = products;
    }

    public Integer getTotalAmount() {
        return products.stream()
                .mapToInt(product -> product.getProduct().getPrice() * product.getQuantity())
                .sum();
    }

    public Integer getSavedPoint() {
        return (int) (getTotalAmount() * POINT_RATE);
    }
}
