package cart.repository.cart;

import cart.dao.cart.CartDao;
import cart.dao.policy.PolicyDao;
import cart.dao.product.ProductDao;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.product.Product;
import cart.entity.cart.CartEntity;
import cart.entity.cart.CartItemEntity;
import cart.entity.policy.PolicyEntity;
import cart.entity.product.ProductEntity;
import cart.exception.CartItemNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartRepository {

    private final CartDao cartDao;
    private final ProductDao productDao;
    private final PolicyDao policyDao;

    public CartRepository(final CartDao cartDao, final ProductDao productDao, final PolicyDao policyDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
        this.policyDao = policyDao;
    }

    public Cart findCartByMemberId(final long memberId) {
        List<CartItemEntity> cartItemEntities = cartDao.findAllCartItemEntitiesByCartId(memberId);

        List<CartItem> cartItems = cartItemEntities.stream()
                .map(cartItemEntity -> {
                    int quantity = cartItemEntity.getQuantity();
                    Long productId = cartItemEntity.getProductId();
                    ProductEntity productEntity = productDao.getProductById(productId);
                    PolicyEntity policyEntity = policyDao.findById(productEntity.getPolicyId());
                    Product product = new Product(productId, productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), policyEntity.isOnSale(), policyEntity.getAmount());
                    return new CartItem(cartItemEntity.getId(), product, quantity);
                })
                .collect(Collectors.toList());

        CartEntity cartEntity = cartDao.findCartEntityByMemberId(memberId);
        return new Cart(cartEntity.getId(), new CartItems(cartItems));
    }

    public Long insertNewCartItem(final Long cartId, final CartItem cartItem) {
        return cartDao.saveCartItem(cartId, cartItem);
    }

    public CartItem findCartItemById(final Long cartItemId) {
        CartItemEntity cartItemEntity = cartDao.findCartItemEntityById(cartItemId)
                .orElseThrow(CartItemNotFoundException::new);

        ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId());
        PolicyEntity policyEntity = policyDao.findById(productEntity.getPolicyId());

        Product product = new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), policyEntity.isOnSale(), policyEntity.getAmount());
        return new CartItem(cartItemId, product, cartItemEntity.getQuantity());
    }

    public void removeCartItem(final CartItem cartItem) {
        cartDao.removeCartItemByCartITemId(cartItem.getId());
    }

    public void updateCartItemQuantity(final CartItem cartItem) {
        cartDao.updateQuantity(cartItem.getId(), cartItem.getQuantity());
    }

    public void deleteCartItemById(final Long cartItemId) {
        cartDao.removeCartItemByCartITemId(cartItemId);
    }

    public void deleteAllByMemberId(final long cartId) {
        cartDao.deleteAllByCartId(cartId);
    }
}
