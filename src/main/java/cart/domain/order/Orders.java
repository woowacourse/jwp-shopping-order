package cart.domain.order;

import cart.domain.member.Member;
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

    public void checkOwner(final Member member) {
        orders.forEach(it -> it.checkOwner(member));
    }
}
