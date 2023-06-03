package cart.domain.respository.cartitem;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DbCartItemRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;

    public DbCartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public CartItem save(final CartItem cartItem) {
        return cartItemDao.save(cartItem);
    }

    @Override
    public Optional<CartItem> findById(final Long id) {
        return cartItemDao.findById(id);
    }

    @Override
    public List<CartItem> findByMemberId(final Long memberId) {
        return cartItemDao.findByMemberId(memberId);
    }

    @Override
    public void updateQuantity(final CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }

    @Override
    public void delete(final Long memberId, final Long productId) {
        cartItemDao.delete(memberId, productId);
    }

    @Override
    public void deleteById(final Long id) {
        cartItemDao.deleteById(id);
    }

    @Override
    public void deleteByMemberIdAndProductIds(final Long memberId, final List<Long> productIds) {
        cartItemDao.deleteByMemberIdAndProductIds(memberId, productIds);
    }
}
