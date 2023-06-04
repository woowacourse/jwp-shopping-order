package cart.repository;

import cart.dao.OrderedItemDao;
import cart.domain.OrderItem;
import cart.entity.OrderedItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderedItemRepository {
    private final OrderedItemDao orderedItemDao;

    public OrderedItemRepository(OrderedItemDao orderedItemDao) {
        this.orderedItemDao = orderedItemDao;
    }

    public void batchSave(List<OrderItem> orderedItems, Long orderId) {
        List<OrderedItemEntity> orderedItemEntities = orderedItems.stream()
                .map(orderedItem -> new OrderedItemEntity(
                        null,
                        orderedItem.getProduct().getId(),
                        orderId,
                        orderedItem.getProduct().getName(),
                        orderedItem.getProduct().getPrice(),
                        orderedItem.getProduct().getImageUrl(),
                        orderedItem.getQuantity()
                ))
                .collect(Collectors.toList());
        orderedItemDao.batchSave(orderedItemEntities);
    }
}
