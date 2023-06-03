package cart.persistence;

import cart.application.repository.OrderRepository;
import cart.domain.Member;
import cart.domain.order.Order;
import cart.persistence.dao.OrderDao;
import cart.persistence.dao.OrderItemDao;
import cart.persistence.dto.OrderDetailDTO;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderItemEntity;
import cart.persistence.mapper.OrderMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderJdbcRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public long order(final Order order) {
        OrderEntity orderEntity = OrderEntity.from(order);
        long orderId = orderDao.create(orderEntity);

        List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                .map(orderItem -> OrderItemEntity.of(orderItem, orderId))
                .collect(Collectors.toList());

        orderItemDao.createAll(orderItemEntities);
        return orderId;
    }

    @Override
    public Optional<Order> findById(final long id) {
        Optional<OrderDetailDTO> optionalOrderDetail = orderDao.findById(id);
        if (optionalOrderDetail.isEmpty()) {
            return Optional.empty();
        }
        OrderDetailDTO orderDetail = optionalOrderDetail.get();
        List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(id);

        Order order = toOrderDomain(orderDetail, orderItemEntities);
        return Optional.of(order);
    }

    @Override
    public List<Order> findByMember(final Member member) {
        List<OrderDetailDTO> orderDetails = orderDao.findByMemberId(member.getId());
        if (orderDetails.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> orderIds = orderDetails.stream()
                .map(orderDetail -> orderDetail.getOrderEntity().getId())
                .collect(Collectors.toList());
        List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderIds(orderIds);
        return toOrders(orderDetails, orderItemEntities);
    }

    private List<Order> toOrders(final List<OrderDetailDTO> orderDetails,
            final List<OrderItemEntity> orderItemEntities) {
        Map<Long, List<OrderItemEntity>> orderIdToItems = orderItemEntities.stream()
                .collect(Collectors.groupingBy(OrderItemEntity::getOrderId));

        return orderDetails.stream()
                .map(orderDetail -> toOrderDomain(orderDetail,
                        orderIdToItems.get(orderDetail.getOrderEntity().getId())))
                .collect(Collectors.toList());
    }

    private Order toOrderDomain(final OrderDetailDTO orderDetail, final List<OrderItemEntity> orderItemEntities) {
        OrderEntity orderEntity = orderDetail.getOrderEntity();
        MemberEntity memberEntity = orderDetail.getMemberEntity();
        MemberCouponEntity memberCouponEntity = orderDetail.getMemberCouponEntity();
        CouponEntity couponEntity = orderDetail.getCouponEntity();
        return OrderMapper.toDomain(orderEntity, memberEntity, memberCouponEntity, couponEntity, orderItemEntities);
    }
}
