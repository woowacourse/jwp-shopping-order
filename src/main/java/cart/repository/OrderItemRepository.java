package cart.repository;

import cart.dao.OrderItemDao;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemRepository {
    private final OrderItemDao orderItemDao;

    public OrderItemRepository(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    public void batchSave(List<OrderItem> orderItems, Long orderId) {
        List<OrderItemEntity> orderItemEntities = orderItems.stream()
                .map(orderItem -> new OrderItemEntity(
                        null,
                        orderItem.getProduct().getId(),
                        orderId,
                        orderItem.getProduct().getName(),
                        orderItem.getProduct().getPrice(),
                        orderItem.getProduct().getImageUrl(),
                        orderItem.getQuantity()
                ))
                .collect(Collectors.toList());
        orderItemDao.batchSave(orderItemEntities);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderId);
        return orderItemEntities.stream()
                .map(orderItemEntity -> new OrderItem(
                        orderItemEntity.getId(),
                        new Product(
                                orderItemEntity.getProductId(),
                                orderItemEntity.getProductNameAtOrder(),
                                orderItemEntity.getProductPriceAtOrder(),
                                orderItemEntity.getProductImageUrlAtOrder()
                        ),
                        orderItemEntity.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
