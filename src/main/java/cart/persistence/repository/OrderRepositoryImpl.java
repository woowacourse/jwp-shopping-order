package cart.persistence.repository;

import static cart.persistence.mapper.OrderMapper.convertOrderEntity;
import static cart.persistence.mapper.OrderMapper.convertOrderProductEntities;
import static cart.persistence.mapper.OrderMapper.convertOrderWithId;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.coupon.CouponWithId;
import cart.domain.member.MemberWithId;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.order.OrderWithId;
import cart.exception.DBException;
import cart.exception.ErrorCode;
import cart.exception.NotFoundException;
import cart.persistence.dao.OrderCouponDao;
import cart.persistence.dao.OrderDao;
import cart.persistence.dao.OrderProductDao;
import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.OrderCouponEntity;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderProductEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderCouponDao orderCouponDao;
    private final OrderProductDao orderProductDao;

    public OrderRepositoryImpl(final OrderDao orderDao, final OrderCouponDao orderCouponDao,
                               final OrderProductDao orderProductDao) {
        this.orderDao = orderDao;
        this.orderCouponDao = orderCouponDao;
        this.orderProductDao = orderProductDao;
    }

    @Override
    public Long save(final Order order) {
        final Long orderId = saveOrder(order);
        final List<CartItemWithId> cartItems = order.getCartItems();
        saveOrderProducts(cartItems, orderId);
        return orderId;
    }

    @Override
    public Long saveWithCoupon(final Order order) {
        final Long orderId = saveOrder(order);
        final List<CartItemWithId> cartItems = order.getCartItems();
        saveOrderProducts(cartItems, orderId);
        saveCoupon(order, orderId);
        return orderId;
    }

    @Override
    public Long countByMemberId(final Long memberId) {
        return orderDao.countByMemberId(memberId);
    }

    @Override
    public OrderWithId getById(final Long id) {
        final List<OrderDto> orderDto = orderDao.findById(id);
        if (orderDto.size() == 0) {
            throw new NotFoundException(ErrorCode.ORDER_NOT_FOUND);
        }
        return convertOrderWithId(orderDto);
    }

    @Override
    public List<OrderWithId> findByMemberName(final String memberName) {
        final List<OrderDto> orderDto = orderDao.findByMemberName(memberName);
        final Map<Long, List<OrderDto>> ordersById = orderDto.stream()
            .collect(Collectors.groupingBy(OrderDto::getOrderId));

        final List<OrderWithId> orderWithIds = new ArrayList<>();
        for (final Long orderId : ordersById.keySet()) {
            final List<OrderDto> detailOrder = ordersById.get(orderId);
            orderWithIds.add(convertOrderWithId(detailOrder));
        }
        return orderWithIds;
    }

    @Override
    public void updateNotValidById(final Long id) {
        final int updatedCount = orderDao.updateNotValidById(id);
        if (updatedCount != 1) {
            throw new DBException(ErrorCode.DB_UPDATE_ERROR);
        }
    }

    private Long saveOrder(final Order order) {
        final MemberWithId member = order.getMember();
        final OrderEntity orderEntity = convertOrderEntity(order, member);
        return orderDao.insert(orderEntity);
    }

    private void saveOrderProducts(final List<CartItemWithId> cartItems, final Long orderId) {
        final List<OrderProductEntity> orderProductEntities = convertOrderProductEntities(cartItems, orderId);
        orderProductDao.saveAll(orderProductEntities);
    }

    private void saveCoupon(final Order order, final Long orderId) {
        final CouponWithId coupon = order.getCoupon()
            .orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));
        final OrderCouponEntity orderCouponEntity = new OrderCouponEntity(orderId, coupon.getCouponId());
        orderCouponDao.insert(orderCouponEntity);
    }
}
