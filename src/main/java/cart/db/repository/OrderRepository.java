package cart.db.repository;

import cart.db.dao.OrderCouponDao;
import cart.db.dao.OrderDao;
import cart.db.dao.OrderProductDao;
import cart.db.entity.*;
import cart.db.mapper.OrderMapper;
import cart.domain.order.Order;
import cart.exception.BadRequestException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cart.exception.ErrorCode.INVALID_ORDER_ID;

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

        if (order.getCoupon().isPresent()) {
            OrderCouponEntity orderCouponEntity = OrderMapper.toEntity(orderId, order.getCoupon().get());
            orderCouponDao.create(orderCouponEntity);
        }
        return orderId;
    }

    public Order findById(final Long orderId) {
        OrderMemberDetailEntity orderMemberDetailEntity = orderDao.findOrderMemberById(orderId)
                .orElseThrow(() -> new BadRequestException(INVALID_ORDER_ID));
        List<OrderProductDetailEntity> orderProductDetailEntities = orderProductDao.findByOrderId(orderId);
        if (orderCouponDao.findByOrderId(orderId).isPresent()) {
            return OrderMapper.toDomain(orderMemberDetailEntity, orderProductDetailEntities, orderCouponDao.findByOrderId(orderId).get());
        }
        return OrderMapper.toDomain(orderMemberDetailEntity, orderProductDetailEntities);
    }


    public List<Order> findByMemberId(final Long memberId) {
        List<OrderMemberDetailEntity> orderEntities = orderDao.findOrderMemberByMemberId(memberId);
        if (orderEntities.size() == 0) {
            return Collections.emptyList();
        }
        List<Long> orderIds = orderEntities.stream()
                .map(OrderMemberDetailEntity::getId)
                .collect(Collectors.toList());
        List<OrderProductDetailEntity> orderProductDetailEntities = orderProductDao.findByOrderIds(orderIds);
        List<OrderCouponDetailEntity> orderCouponDetailEntities = orderCouponDao.findByOrderIds(orderIds);
        return OrderMapper.toDomain(orderEntities, orderProductDetailEntities, orderCouponDetailEntities);
    }
}
