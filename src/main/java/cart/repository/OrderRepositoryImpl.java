package cart.repository;

import cart.dao.OrderDao;
import cart.domain.Order;
import cart.domain.repository.OrderRepository;
import cart.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDao orderDao;

    public OrderRepositoryImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Long saveOrder(Order order) {
        return orderDao.saveOrder(toEntity(order));
    }

    private OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.getMember().getId(),
                order.calculatePrice(),
                order.calculateDiscountPrice());
    }
}
