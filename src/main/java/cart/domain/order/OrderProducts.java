package cart.domain.order;

import java.util.List;

class OrderProducts {

    private final List<OrderProduct> value;

    public OrderProducts(List<OrderProduct> value) {
        this.value = value;
    }

    public int calculateTotalPrice() {
        return value.stream()
                .mapToInt(OrderProduct::calculateTotalPrice)
                .sum();
    }

    public List<OrderProduct> getValue() {
        return value;
    }
}
