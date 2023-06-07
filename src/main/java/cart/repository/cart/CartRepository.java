package cart.repository.cart;

import cart.dao.cart.CartDao;
import cart.dao.product.ProductDao;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.product.Product;
import cart.entity.cart.CartEntity;
import cart.entity.cart.CartItemEntity;
import cart.entity.product.ProductEntity;
import cart.exception.CartItemNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartRepository {

    private final CartDao cartDao;
    private final ProductDao productDao;

    public CartRepository(final CartDao cartDao, final ProductDao productDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
    }

    public Cart findCartByMemberId(final long memberId) {
        CartEntity cartEntity = cartDao.findCartEntityByMemberId(memberId);
        return getCart(cartEntity);
    }

    public Long insertNewCartItem(final Cart cart, final Long productId) {
        return cartDao.saveCartItem(cart.getId(), productId);
    }

    public CartItem findCartItemById(final Long cartItemId) {
        CartItemEntity cartItemEntity = cartDao.findCartItemEntityById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        ProductEntity productEntity = productDao.getProductById(cartItemEntity.getProductId());

        Product product = new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productEntity.getSalePrice());
        return new CartItem(cartItemId, product, cartItemEntity.getQuantity());
    }

    public void removeCartItem(final CartItem cartItem) {
        cartDao.removeCartItemByCartITemId(cartItem.getId());
    }

    public void updateCartItemQuantity(final CartItem cartItem) {
        cartDao.updateQuantity(cartItem.getId(), cartItem.getQuantity());
    }

    public void updateCartItemQuantity(final CartItem cartItem, final int quantity) {
        cartDao.updateQuantity(cartItem.getId(), quantity);
    }

    public void addCartItemQuantity(final CartItem cartItem) {
        cartDao.updateQuantity(cartItem.getId(), cartItem.getQuantity() + 1);
    }

    public void deleteCartItemById(final Long cartItemId) {
        cartDao.removeCartItemByCartITemId(cartItemId);
    }

    public long createMemberCart(final long memberId) {
        return cartDao.createMemberCart(memberId);
    }

    public void deleteAllCartItems(final Cart cart) {
        cartDao.deleteAllCartItemsByCartId(cart.getId());
    }

    public boolean isExistAlreadyCartItem(final Cart cart, final Product product) {
        return cartDao.isExistAlreadyCartItem(cart.getId(), product.getId());
    }

    public CartItem findCartItem(final Cart cart, final Long productId) {
        CartItemEntity cartItemEntity = cartDao.findCartItem(cart.getId(), productId);
        ProductEntity productEntity = productDao.findById(cartItemEntity.getProductId());
        Product product = new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productEntity.getSalePrice());
        return new CartItem(cartItemEntity.getId(), product, cartItemEntity.getQuantity());
    }

    public Cart findCartByCartItemId(final Long cartItemId) {
        CartItemEntity cartItem = cartDao.findCartItemEntityById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        CartEntity cartEntity = cartDao.findCartEntityById(cartItem.getCartId());
        return getCart(cartEntity);
    }

    private Cart getCart(final CartEntity cartEntity) {
        List<CartItemEntity> cartItemEntities = cartDao.findAllCartItemEntitiesByCartId(cartEntity.getId());

        List<CartItem> cartItems = cartItemEntities.stream()
                .map(cartItemEntity -> {
                    int quantity = cartItemEntity.getQuantity();
                    Long productId = cartItemEntity.getProductId();
                    ProductEntity productEntity = productDao.getProductById(productId);
                    Product product = new Product(productId, productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productEntity.getSalePrice());
                    return new CartItem(cartItemEntity.getId(), product, quantity);
                })
                .collect(Collectors.toList());

        return new Cart(cartEntity.getId(), cartEntity.getMemberId(), new CartItems(cartItems));
    }
}
