package cart.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import cart.domain.Order;

@Repository
public class OrderDao {

    public List<Order> findAllByMemberId(Long id) {
        return Collections.emptyList();
    }

    public Order findById(Long id) {
        return null;
    }

    public Long save(Order order) {
        return null;
    }

    public void update(Order order) {
    }
}
