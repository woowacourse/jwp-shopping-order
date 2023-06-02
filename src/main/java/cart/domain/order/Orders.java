package cart.domain.order;

import java.util.ArrayList;
import java.util.List;

public class Orders {

    private final List<Order> orders;

    public Orders(final List<Order> orders) {
        this.orders = new ArrayList<>(orders);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
