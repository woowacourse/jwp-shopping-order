package cart.domain.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCartItemRepository implements CartItemRepository {
    private final CartItemDao cartItemDao;

    public JdbcCartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public CartItem findCartItemById(final Long id) {
        return cartItemDao.findById(id);
    }

    @Override
    public void deleteCartItemById(final Long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public List<CartItem> findByMemberId(final Long memberId) {
        return cartItemDao.findByMemberId(memberId);
    }

    @Override
    public CartItem findByMemberIdAndProductId(final Long memberId, final Long productId) {
        return cartItemDao.findByMemberIdAndProductId(memberId, productId);
    }

    @Override
    public Long save(final CartItem cartItem) {
        return cartItemDao.save(cartItem);
    }

    @Override
    public void updateQuantity(final CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }
}
