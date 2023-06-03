package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.dao.entity.CartItemEntity;
import cart.domain.ProductQuantity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartItemRepository {
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public CartItemRepository(CartItemDao cartItemDao,  ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public List<ProductQuantity> changeCartItemToOrdersItemAndGetProductQuantities(final List<Long> cartIds) {
        CartItemEntity cartItem;
        List<ProductQuantity> productQuantities = new ArrayList<>();
        for (Long cartId : cartIds) {
            cartItem = cartItemDao.findCartItemEntitiesByCartId(cartId);
            productQuantities.add(new ProductQuantity(cartItem.getProductId(),cartItem.getQuantity()));
            cartItemDao.deleteById(cartId);
        }
        return productQuantities;
    }

    public int findTotalPriceByCartId(final long cartId) {
        CartItemEntity cartItem = cartItemDao.findCartItemEntitiesByCartId(cartId);
        return productDao.findPriceById(cartItem.getProductId()) * cartItem.getQuantity();
    }
}
