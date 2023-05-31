package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.OrdersCartItemDao;
import cart.dao.ProductDao;
import cart.dao.entity.CartItemEntity;
import cart.dao.entity.OrdersCartItemEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            cartItemDao.deleteById(cartId);
            ordersCartItemDao.createOrdersIdCartItemId(orderId, cartItem.getProductId(), cartItem.getQuantity());
        }
    }
    public List<Long> findProductItemsByCartItemIds(final List<Long> cartItemIds){
        return cartItemIds.stream()
                .map(id -> cartItemDao.findProductIdByCartId(id))
                .collect(Collectors.toList());
    }

    public Map<Long,Integer> findProductQuantityByCartItemIds(final List<Long> cartItemIds){
        List<CartItemEntity> cartItemEntities = cartItemIds.stream()
                .map(id -> cartItemDao.findCartItemEntitiesByCartId(id))
                .collect(Collectors.toList());
        Map<Long,Integer> productIdQuantityMap = new LinkedHashMap<>();
        for(CartItemEntity cartItem: cartItemEntities){
            productIdQuantityMap.put(cartItem.getProductId(),cartItem.getQuantity());
        }
        return productIdQuantityMap;
    }

    public List<CartItem> findCartItemsByOrderId(Member member, final long orderId){
        return   ordersCartItemDao.findAllByOrdersId(orderId).stream()
                .map(ordersCartItem -> findProductWithCartItems(member, ordersCartItem))
                .collect(Collectors.toList());
    }
    private CartItem findProductWithCartItems(Member member, final OrdersCartItemEntity ordersCartItem) {
        return new CartItem(ordersCartItem.getId(),
                ordersCartItem.getQuantity(),
                productDao.findById(ordersCartItem.getProductId()),
                member);
    }
}
