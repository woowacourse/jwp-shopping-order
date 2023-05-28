package cart.cart.domain.cartitem.application;

import cart.cart.domain.cartitem.CartItem;
import cart.cart.domain.cartitem.infrastructure.CartItemDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartItemRepositoryDaoImpl implements CartItemRepository {
    private final CartItemDao cartItemDao;

    public CartItemRepositoryDaoImpl(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public List<CartItem> findByMemberId(Long memberId) {
        return cartItemDao.findByMemberId(memberId);
    }

    @Override
    public Long save(CartItem cartItem) {
        return cartItemDao.save(cartItem);
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemDao.findById(id);
    }

    @Override
    public void delete(Long memberId, Long productId) {
        cartItemDao.delete(memberId, productId);
    }

    @Override
    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }
}
