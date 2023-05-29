package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrdersCartItemDao;
import cart.dao.OrdersCouponDao;
import cart.dao.OrdersDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.Orders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdersRepository {
    private final OrdersDao ordersDao;
    private final CartItemDao cartItemDao;
    private final OrdersCouponDao ordersCouponDao;
    private final OrdersCartItemDao ordersCartItemDao;

    public OrdersRepository(OrdersDao ordersDao, CartItemDao cartItemDao, OrdersCouponDao ordersCouponDao, OrdersCartItemDao ordersCartItemDao) {
        this.ordersDao = ordersDao;
        this.cartItemDao = cartItemDao;
        this.ordersCouponDao = ordersCouponDao;
        this.ordersCartItemDao = ordersCartItemDao;
    }

    public long takeOrders(
            final Long memberId,
            final List<Long> cartIds,
            final int originalPrice,
            final int discountPrice,
            final List<Long> couponIds){
        final long orderId = ordersDao.createOrders(memberId,originalPrice,discountPrice);
        CartItemEntity cartItem;
        for(Long cartId: cartIds){
            cartItem = cartItemDao.findProductIdByCartId(cartId);
            cartItemDao.deleteById(cartId);
            ordersCartItemDao.createOrdersIdCartItemId(orderId,cartItem.getProductId(),cartItem.getQuantity());
        }
        for(Long couponId: couponIds){
            ordersCouponDao.createOrderCoupon(orderId,couponId);
        }
        return orderId;
    }
}
