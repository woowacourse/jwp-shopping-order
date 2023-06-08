package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.cartitem.CartItem;
import cart.exception.business.cartitem.InvalidCartItemsException;
import cart.exception.notfound.CartItemNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Long save(final CartItem cartItem) {
        final Optional<CartItem> cartItemOptional = cartItemDao.findById(cartItem.getId());
        if (cartItemOptional.isPresent()) {
            return cartItemOptional.get().getId();
        }
        return cartItemDao.insert(cartItem);
    }

    public CartItem findById(final Long id) {
        return cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException(id));
    }

    public List<CartItem> findAllByMemberId(final Long id) {
        return cartItemDao.findAllByMemberId(id);
    }

    public List<CartItem> findAllByIds(final List<Long> ids) {
        final List<CartItem> cartItems = cartItemDao.findAllByIds(ids);
        if (ids.size() != cartItems.size()) {
            throw new InvalidCartItemsException(ids);
        }
        return cartItems;
    }

    public void update(final CartItem cartItem) {
        cartItemDao.update(cartItem);
    }

    public void delete(final CartItem cartItem) {
        cartItemDao.delete(cartItem.getId());
    }

    public void deleteAll(final List<CartItem> cartItems) {
        final List<Long> ids = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        cartItemDao.deleteByIds(ids);
    }

    public void deleteByIds(final List<Long> ids) {
        cartItemDao.deleteByIds(ids);
    }
}
