package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.Orders;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.ProductEntity;
import cart.repository.mapper.OrderMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final MemberDao memberDao;
    private final ProductDao productDao;
    private final OrderItemDao orderItemDao;
    private final OrderDao orderDao;

    public OrderRepository(
            final MemberDao memberDao,
            final ProductDao productDao,
            final OrderItemDao orderItemDao,
            final OrderDao orderDao
    ) {
        this.memberDao = memberDao;
        this.productDao = productDao;
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
    }

    public Orders findByMember(final Member member) {
        final MemberEntity orderOwner = memberDao.getMemberById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException(member.getId() + "id를 가진 멤버를 찾을 수 없습니다.")
        );
        final List<OrderEntity> orderEntities = orderDao.findOrderByMemberId(member.getId());

        if (orderEntities.isEmpty()) {
            return new Orders(Collections.emptyList());
        }

        final List<Long> orderIds = getOrderIds(orderEntities);
        final Map<Long, List<OrderItemEntity>> orderItemGroupById = orderItemDao.findGroupByOrderId(orderIds);

        final List<Long> allProductIds = getAllProductId(orderItemGroupById);
        final Map<Long, ProductEntity> productGroupById = productDao.getProductGroupById(allProductIds);

        return new Orders(orderEntities.stream()
                .map(it -> OrderMapper.toOrder(
                        it,
                        orderItemGroupById.get(it.getId()),
                        productGroupById,
                        orderOwner)
                ).collect(Collectors.toUnmodifiableList()));
    }

    private List<Long> getAllProductId(final Map<Long, List<OrderItemEntity>> orderItemGroupById) {
        return orderItemGroupById.values().stream()
                .flatMap(Collection::stream)
                .map(OrderItemEntity::getProductId)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> getOrderIds(final List<OrderEntity> orderEntities) {
        return orderEntities.stream()
                .map(OrderEntity::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public Order findByOrderId(final Long orderId) {
        final OrderEntity orderEntity = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(orderId + "id를 가진 주문을 찾을 수 없습니다."));

        final List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(orderId);

        final List<Long> productIds = getAllProductId(orderItemEntities);
        final Map<Long, ProductEntity> productGroupById = productDao.getProductGroupById(productIds);

        final MemberEntity memberEntity = memberDao.getMemberById(orderEntity.getMemberId())
                .orElseThrow(() -> new IllegalStateException("알 수 없는 오류 발생"));

        return OrderMapper.toOrder(
                orderEntity,
                orderItemEntities,
                productGroupById,
                memberEntity
        );
    }

    private List<Long> getAllProductId(final List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(OrderItemEntity::getProductId)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long save(final Order order) {
        final Long orderId = orderDao.save(OrderMapper.toOrderEntity(order));
        orderItemDao.batchInsert(OrderMapper.toOrderItemEntities(order.getOrderItems(), orderId));

        return orderId;
    }
}
