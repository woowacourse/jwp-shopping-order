package cart.repository;

import cart.dao.*;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.OrdersCartItemEntity;
import cart.dao.entity.OrdersEntity;
import cart.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdersRepository {
    private final OrdersDao ordersDao;
    private final CartItemDao cartItemDao;
    private final OrdersCouponDao ordersCouponDao;
    private final OrdersCartItemDao ordersCartItemDao;
    private final ProductDao productDao;
    private final CouponDao couponDao;

    public OrdersRepository(OrdersDao ordersDao,
                            CartItemDao cartItemDao,
                            OrdersCouponDao ordersCouponDao,
                            OrdersCartItemDao ordersCartItemDao,
                            ProductDao productDao,
                            CouponDao couponDao) {
        this.ordersDao = ordersDao;
        this.cartItemDao = cartItemDao;
        this.ordersCouponDao = ordersCouponDao;
        this.ordersCartItemDao = ordersCartItemDao;
        this.productDao = productDao;
        this.couponDao = couponDao;
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

    public List<Orders> findAllOrdersByMember(Member member){
        return ordersDao.findAllByMemberId(member.getId()).stream()
                .map(ordersEntity -> makeOrders(ordersEntity,member))
                .collect(Collectors.toList());
    }
    private Orders makeOrders(OrdersEntity ordersEntity,Member member){
        List<CartItem> cartItems = ordersCartItemDao.findAllByOrdersId(ordersEntity.getId()).stream()
                .map(ordersCartItem -> findProductWithCartItems(member,ordersCartItem))
                .collect(Collectors.toList());
        List<Coupon> coupons = ordersCouponDao.finAllByOrdersId(ordersEntity.getId()).stream()
                .map(ordersCouponEntity -> couponDao.findWithId(ordersCouponEntity.getCouponId()))
                .collect(Collectors.toList());
        return new Orders(ordersEntity.getId(),member,cartItems,ordersEntity.getOriginalPrice(),ordersEntity.getDiscountPrice(),coupons,ordersEntity.isConfirmState());
    }
    private CartItem findProductWithCartItems(Member member,final OrdersCartItemEntity ordersCartItem){
        return new CartItem(member,productDao.getProductById(ordersCartItem.getProductId()));
    }
    public Orders findOrdersById(final Member member,final long id){
        return makeOrders(ordersDao.findById(id),member);
    }
}
