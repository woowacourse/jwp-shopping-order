package cart.repository;

import java.util.List;
import java.util.stream.Collectors;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.CartItemNotFoundException;
import cart.exception.ProductNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemRepository(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public CartItem addCartItem(final Member member, final Long productId) {
        final Product product = productDao.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId))
                .toProduct();
        return cartItemDao.insert(CartItem.of(member, product));
    }

    public List<CartItem> findByMember(final Member member) {
        return cartItemDao.findByMemberId(member.getId());
    }

    public CartItems findByIds(final Member member, final List<Long> ids) {
        return CartItems.of(ids.stream()
                .map(id -> getOwnedCartItem(member, id))
                .collect(Collectors.toList()));
    }

    private CartItem getOwnedCartItem(final Member member, final Long id) {
        final CartItem cartItem = cartItemDao.findById(id).orElseThrow(() -> new CartItemNotFoundException(id));
        cartItem.checkOwner(member);
        return cartItem;
    }

    public void updateQuantity(final Member member, final Long id, final int quantity) {
        final CartItem cartItem = getOwnedCartItem(member, id);
        updateQuantity(cartItem, quantity);
    }

    private void updateQuantity(final CartItem cartItem, final int quantity) {
        if (quantity == 0) {
            cartItemDao.deleteById(cartItem.getId());
            return;
        }
        final CartItem updatedCartItem = cartItem.changeQuantity(quantity);
        cartItemDao.updateQuantity(updatedCartItem);
    }

    public void deleteCartItem(final Member member, final Long id) {
        final CartItem cartItem = getOwnedCartItem(member, id);
        cartItemDao.deleteById(cartItem.getId());
    }
}
