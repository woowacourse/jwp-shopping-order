package cart.repository;

import cart.dao.OrderDao;
import cart.domain.Order;
import cart.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderedItemRepository orderedItemRepository;

    public OrderRepository(OrderDao orderDao, OrderedItemRepository orderedItemRepository) {
        this.orderDao = orderDao;
        this.orderedItemRepository = orderedItemRepository;
    }

    public Long save(Order order) {
        Long orderId = orderDao.save(convertToEntity(order));
        orderedItemRepository.batchSave(order.getOrderItems(), orderId);

        return orderId;
    }

    private OrderEntity convertToEntity(Order order) {
        return new OrderEntity(
                order.getId(),
                order.getMember().getId(),
                order.getUsedPoint().getPoint(),
                order.getSavedPoint().getPoint(),
                order.getOrderedAt()
        );
    }
}
