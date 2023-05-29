package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrdersDao;
import cart.domain.Orders;
import org.springframework.stereotype.Component;

@Component
public class OrdersRepository {
    private final OrdersDao ordersDao;
    private final CartItemDao cartItemDao;

    public OrdersRepository(OrdersDao ordersDao, CartItemDao cartItemDao) {
        this.ordersDao = ordersDao;
        this.cartItemDao = cartItemDao;
    }

    public long takeOrders(final Orders orders){
        for(Long cartId: orders.getCartId()){
            cartItemDao.deleteById(cartId);
        }
        return ordersDao.createOrders(orders.getMemberId(),orders.getOriginalPrice(),orders.getDiscountPrice());
    }
}
