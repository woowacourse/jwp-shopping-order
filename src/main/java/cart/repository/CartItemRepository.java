package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrdersCartItemDao;
import cart.dao.ProductDao;
import cart.dao.entity.CartItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartItemRepository {
    private final CartItemDao cartItemDao;
    private final OrdersCartItemDao ordersCartItemDao;
    private final ProductDao productDao;

    public CartItemRepository(CartItemDao cartItemDao, OrdersCartItemDao ordersCartItemDao, ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.ordersCartItemDao = ordersCartItemDao;
        this.productDao = productDao;
    }

    public void changeCartItemToOrdersItem(final long orderId, final List<Long> cartIds) {
        CartItemEntity cartItem;
        for (Long cartId : cartIds) {
            cartItem = cartItemDao.findCartItemEntitiesByCartId(cartId);
            ordersCartItemDao.createOrdersIdCartItemId(orderId, cartItem.getProductId(), cartItem.getQuantity());
            cartItemDao.deleteById(cartId);
        }
    }

    public int findTotalPriceByCartId(final long cartId) {
        CartItemEntity cartItem = cartItemDao.findCartItemEntitiesByCartId(cartId);
        return productDao.findPriceById(cartItem.getProductId()) * cartItem.getQuantity();
    }
}
