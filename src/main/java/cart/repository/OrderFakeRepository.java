package cart.repository;

import cart.domain.Member;
import cart.domain.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderFakeRepository implements OrderRepository {

    private final List<Order> orders;

    public OrderFakeRepository() {
        this.orders = new ArrayList<>();
    }

    @Override
    public Long save(Order order) {
        Order orderWithId = new Order((long) orders.size() + 1, order.getMember(), order.getOrderItems());
        orders.add(orderWithId);
        return orderWithId.getId();
    }

    @Override
    public List<Order> findByMember(Member member) {
        return orders.stream()
                .filter(order -> order.getMember().equals(member))
                .collect(Collectors.toUnmodifiableList());

    }

    @Override
    public Optional<Order> findById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }
}
