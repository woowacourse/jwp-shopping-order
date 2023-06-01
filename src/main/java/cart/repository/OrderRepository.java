package cart.repository;

import cart.dao.OrderCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.member.MemberCoupon;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.product.Product;
import cart.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderCouponDao orderCouponDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao, final OrderCouponDao orderCouponDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderCouponDao = orderCouponDao;
    }

    public Long create(List<OrderItem> orderItems, Long memberId) {
        Long orderId = orderDao.create(memberId);

        for (OrderItem orderItem : orderItems) {
            Long orderItemId = orderItemDao.create(orderId, orderItem.getProduct(), orderItem.getQuantity());
            for (MemberCoupon coupon : orderItem.getCoupons()) {
                orderCouponDao.create(orderItemId, coupon.getId());
            }
        }
        return orderId;
    }

    public List<Order> findAllByMemberId(Long memberId) {
        List<Long> orderIds = orderDao.findAllByMemberId(memberId);

        List<Order> orders = new ArrayList<>();

        for (Long orderId : orderIds) {
            List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderId(orderId);
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemEntity orderItem : orderItemEntities) {
                List<MemberCoupon> coupons = orderCouponDao.findCouponsByOrderItemId(orderItem.getId());
                Product product = orderItem.getProduct();
                orderItems.add(new OrderItem(
                        orderItem.getId(),
                        new Product(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()),
                        orderItem.getQuantity(),
                        coupons
                ));
            }
            orders.add(new Order(orderId, orderItems));
        }
        return orders;
    }

    public Order findById(Long id) {
        Long orderId = orderDao.findAllById(id);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemEntity orderItem : orderItemDao.findAllByOrderId(orderId)) {
            List<MemberCoupon> coupons = orderCouponDao.findCouponsByOrderItemId(orderItem.getId());
            Product product = orderItem.getProduct();
            orderItems.add(new OrderItem(
                    orderItem.getId(),
                    new Product(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()),
                    orderItem.getQuantity(),
                    coupons
            ));
        }
        return new Order(orderId, orderItems);
    }
}
