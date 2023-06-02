package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Long save(final Order order) {
        final Long orderId = orderDao.save(toEntity(order));
        final List<OrderItemEntity> orderItemEntities = toEntity(order.getOrderItems(), orderId);
        orderItemDao.saveAll(orderItemEntities);
        return orderId;
    }

    public Order findById(final Long id, final Member member) {
        final OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 주문 정보가 없습니다."));
        return toDomain(orderEntity, member);
    }

    public List<Order> findByMember(final Member member) {
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(member.getId());
        return orderEntities.stream()
                .map(orderEntity -> toDomain(orderEntity, member))
                .collect(Collectors.toUnmodifiableList());
    }

    private Order toDomain(final OrderEntity orderEntity, final Member member) {
        return new Order(
                orderEntity.getId(),
                member,
                toDomain(orderItemDao.findByOrderId(orderEntity.getId())),
                orderEntity.getUsedPoint(),
                orderEntity.getSavedPoint(),
                orderEntity.getOrderedAt()
                );
    }

    private OrderEntity toEntity(final Order order) {
        return new OrderEntity(
                order.getId(),
                order.getMember().getId(),
                order.getOrderedAt(),
                order.getUsedPoint(),
                order.getSavedPoint()
        );
    }

    private List<OrderItemEntity> toEntity(final List<OrderItem> orderItems, final Long orderId) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemEntity(
                        orderItem.getId(),
                        orderId,
                        orderItem.getProduct().getId(),
                        orderItem.getProduct().getName(),
                        orderItem.getProduct().getPrice(),
                        orderItem.getProduct().getImageUrl(),
                        orderItem.getQuantity()
                ))
                .collect(Collectors.toUnmodifiableList());
    }

    private List<OrderItem> toDomain(final List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(orderItemEntity -> new OrderItem(
                        orderItemEntity.getId(),
                        orderItemEntity.getQuantity(),
                        new Product(
                                orderItemEntity.getProductId(),
                                orderItemEntity.getProductNameAtOrder(),
                                orderItemEntity.getProductPriceAtOrder(),
                                orderItemEntity.getProductImageUrlAtOrder()
                        )
                ))
                .collect(Collectors.toUnmodifiableList());
    }
}
