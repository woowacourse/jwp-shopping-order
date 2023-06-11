package cart.repository;

import cart.dao.OrderDao;
import cart.domain.Order;
import cart.domain.OrderItems;
import cart.domain.Point;
import cart.entity.OrderEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;

    public OrderRepository(OrderDao orderDao, OrderItemRepository orderItemRepository,
                           MemberRepository memberRepository) {
        this.orderDao = orderDao;
        this.orderItemRepository = orderItemRepository;
        this.memberRepository = memberRepository;
    }

    public Long save(Order order) {
        Long orderId = orderDao.save(convertToEntity(order));
        orderItemRepository.batchSave(order.getOrderItemsByList(), orderId);

        return orderId;
    }

    public List<Order> findByMemberId(Long memberId) {
        List<OrderEntity> orderEntities = orderDao.findByMemberId(memberId);
        return orderEntities.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    public Order findById(Long id) {
        OrderEntity orderEntity = orderDao.findById(id);
        return convertToDomain(orderEntity);
    }

    private Order convertToDomain(OrderEntity orderEntity) {
        return new Order(
                orderEntity.getId(),
                memberRepository.findById(orderEntity.getMemberId()),
                new OrderItems(orderItemRepository.findByOrderId(orderEntity.getId())),
                new Point(orderEntity.getUsedPoint()),
                new Point(orderEntity.getSavedPoint()),
                orderEntity.getOrderedAt()
        );
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
