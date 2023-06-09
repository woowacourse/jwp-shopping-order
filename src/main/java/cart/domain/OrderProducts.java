package cart.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderProducts {
    private final List<OrderProduct> orderProducts;

    public OrderProducts(final List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Price getTotalPrice() {
        int totalPrice = orderProducts.stream()
                .mapToInt(orderProduct -> {
                    Product product = orderProduct.getProduct();
                    return product.getPrice() * orderProduct.getQuantity();
                })
                .sum();

        return new Price(totalPrice);
    }

    public List<OrderProduct> getOrderProducts() {
        return new ArrayList<>(orderProducts);
    }
}
