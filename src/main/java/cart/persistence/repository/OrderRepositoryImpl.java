package cart.persistence.repository;

import static cart.persistence.mapper.OrderMapper.convertOrderEntity;
import static cart.persistence.mapper.OrderMapper.convertOrderProductEntities;

import cart.domain.cartitem.CartItemWithId;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.dto.MemberWithId;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.persistence.dao.OrderCouponDao;
import cart.persistence.dao.OrderDao;
import cart.persistence.dao.OrderProductDao;
import cart.persistence.entity.OrderCouponEntity;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderProductEntity;
import java.util.List;
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
    public void getById(final Long id) {
        orderDao.findById(id);
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
        final CouponWithId coupon = order.getCoupon();
        final OrderCouponEntity orderCouponEntity = new OrderCouponEntity(orderId, coupon.getCouponId());
        orderCouponDao.insert(orderCouponEntity);
    }
}
