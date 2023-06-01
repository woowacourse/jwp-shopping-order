package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.Item;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;

    public OrderRepository(OrderDao orderDao, OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
    }

    public Order save(Order order) {
        Member member = order.getMember();
        Money deliveryFee = order.getDeliveryFee();
        Long savedOrderId = orderDao.save(toEntity(member, deliveryFee));

        List<OrderProductEntity> orderProductEntities = toEntities(order, savedOrderId);
        orderProductDao.saveAll(orderProductEntities);
        return new Order(savedOrderId, member, order.getItems(), deliveryFee);
    }

    private OrderEntity toEntity(Member member, Money deliveryFee) {
        return new OrderEntity(member.getId(), deliveryFee.getValue().intValue());
    }

    private List<OrderProductEntity> toEntities(Order order, Long savedOrderId) {
        return order.getItems().stream()
                .map(item -> toEntity(savedOrderId, item))
                .collect(Collectors.toList());
    }

    private OrderProductEntity toEntity(Long savedOrderId, Item item) {
        int quantity = item.getQuantity();
        Product product = item.getProduct();
        return new OrderProductEntity(
                savedOrderId,
                product.getId(),
                quantity,
                product.getName(),
                product.getPrice().getValue(),
                product.getImageUrl()
        );
    }
}
