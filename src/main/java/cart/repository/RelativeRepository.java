package cart.repository;

import cart.dao.CouponDao;
import cart.dao.OrdersCartItemDao;
import cart.dao.OrdersCouponDao;
import cart.dao.ProductDao;
import cart.dao.entity.OrdersCartItemEntity;
import cart.dao.entity.OrdersEntity;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Orders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelativeRepository {
    private final OrdersCouponDao ordersCouponDao;
    private final OrdersCartItemDao ordersCartItemDao;
    private final ProductDao productDao;
    private final CouponDao couponDao;

    public RelativeRepository(OrdersCouponDao ordersCouponDao, OrdersCartItemDao ordersCartItemDao, ProductDao productDao, CouponDao couponDao) {
        this.ordersCouponDao = ordersCouponDao;
        this.ordersCartItemDao = ordersCartItemDao;
        this.productDao = productDao;
        this.couponDao = couponDao;
    }

    public Orders makeOrders(OrdersEntity ordersEntity, Member member) {
        List<CartItem> cartItems = ordersCartItemDao.findAllByOrdersId(ordersEntity.getId()).stream()
                .map(ordersCartItem -> findProductWithCartItems(member, ordersCartItem))
                .collect(Collectors.toList());
        List<Coupon> coupons = ordersCouponDao.finAllByOrdersId(ordersEntity.getId()).stream()
                .map(ordersCouponEntity -> couponDao.findWithId(ordersCouponEntity.getCouponId()))
                .collect(Collectors.toList());
        return new Orders(ordersEntity.getId(), member, cartItems, ordersEntity.getOriginalPrice(), ordersEntity.getDiscountPrice(), coupons, ordersEntity.isConfirmState());
    }

    private CartItem findProductWithCartItems(Member member, final OrdersCartItemEntity ordersCartItem) {
        return new CartItem(member, productDao.findById(ordersCartItem.getProductId()));
    }

    public void addOrdersCoupon(final long orderId, final List<Long> couponIds) {
        for (Long couponId : couponIds) {
            ordersCouponDao.createOrderCoupon(orderId, couponId);
        }
    }
}
