package cart.db.repository;

import cart.db.dao.OrderCouponDao;
import cart.db.dao.OrderDao;
import cart.db.dao.OrderProductDao;
import cart.db.entity.*;
import cart.db.mapper.OrderMapper;
import cart.domain.order.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final OrderCouponDao orderCouponDao;

    public OrderRepository(final OrderDao orderDao, final OrderProductDao orderProductDao, final OrderCouponDao orderCouponDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.orderCouponDao = orderCouponDao;
    }

    @Transactional
    public Long save(final Order order) {
        OrderEntity orderEntity = OrderMapper.toEntity(order);
        Long orderId = orderDao.create(orderEntity);

        List<OrderProductEntity> orderProductEntities = OrderMapper.toEntity(orderId, order.getItems());
        orderProductDao.createAll(orderProductEntities);

        OrderCouponEntity orderCouponEntity = OrderMapper.toEntity(orderId, order.getCoupon());
        orderCouponDao.create(orderCouponEntity);
        return orderId;
    }

    public Order findById(final Long orderId) {
        OrderMemberDetailEntity orderMemberDetailEntity = orderDao.findOrderMemberById(orderId);
        List<OrderProductDetailEntity> orderProductDetailEntities = orderProductDao.findByOrderId(orderId);
        OrderCouponDetailEntity orderCouponDetailEntity = orderCouponDao.findByOrderId(orderId);
        return OrderMapper.toDomain(orderMemberDetailEntity, orderProductDetailEntities, orderCouponDetailEntity);
    }


    public List<Order> findByMemberId(final Long memberId) {
        List<OrderMemberDetailEntity> orderEntities = orderDao.findOrderMemberByMemberId(memberId);
        List<Long> orderIds = orderEntities.stream()
                .map(OrderMemberDetailEntity::getId)
                .collect(Collectors.toList());
        List<OrderProductDetailEntity> orderProductDetailEntities = orderProductDao.findByOrderIds(orderIds);
        List<OrderCouponDetailEntity> orderCouponDetailEntities = orderCouponDao.findByOrderIds(orderIds);
        return OrderMapper.toDomain(orderEntities, orderProductDetailEntities, orderCouponDetailEntities);
    }
}
