package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrdersCartItemDao;
import cart.dao.entity.CartItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartRepository {
    private final CartItemDao cartItemDao;
    private final OrdersCartItemDao ordersCartItemDao;

    public CartRepository(CartItemDao cartItemDao, OrdersCartItemDao ordersCartItemDao) {
        this.cartItemDao = cartItemDao;
        this.ordersCartItemDao = ordersCartItemDao;
    }
    public void changeCartItemToOrdersItem(final long orderId, final List<Long> cartIds){
        CartItemEntity cartItem;
        for(Long cartId: cartIds){
            cartItem = cartItemDao.findProductIdByCartId(cartId);
            cartItemDao.deleteById(cartId);
            ordersCartItemDao.createOrdersIdCartItemId(orderId,cartItem.getProductId(),cartItem.getQuantity());
        }
    }

}
