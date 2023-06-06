package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.exception.ResourceNotFoundException;
import cart.ui.pageable.Page;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryWithDao implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final MemberDao memberDao;

    public OrderRepositoryWithDao(final OrderDao orderDao, final OrderItemDao orderItemDao, final MemberDao memberDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
    }

    public Long save(final Order order) {
        final Long orderId = orderDao.save(toEntity(order));
        final List<OrderItemEntity> orderItemEntities = toEntity(order.getOrderItems(), orderId);
        orderItemDao.saveAll(orderItemEntities);
        return orderId;
    }

    public Order findById(final Long id) {
        final OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 주문 정보가 없습니다."));
        final MemberEntity member = memberDao.findById(orderEntity.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("주문 정보의 사용자 정보가 올바르지 않습니다."));
        return toDomain(
                orderEntity,
                new Member(
                        member.getId(),
                        member.getEmail(),
                        member.getPassword(),
                        member.getPoint()
                )
        );
    }

    public List<Order> findByMember(final Member member, final Page page) {
        final List<OrderEntity> orderEntities = orderDao.findByMemberIdWithPaging(
                member.getId(),
                page
        );
        return toDomain(orderEntities, member);
    }

    private List<Order> toDomain(final List<OrderEntity> orderEntities, final Member member) {
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
