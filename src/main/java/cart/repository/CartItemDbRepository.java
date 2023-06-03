package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartItemDbRepository implements CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemDbRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public Optional<CartItem> findById(final Long id) {
        try {
            return Optional.ofNullable(this.cartItemDao.findById(id));
        } catch (final DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<CartItem> findByMember(final Member member) {
        return this.cartItemDao.findByMemberId(member.getId());
    }

    @Override
    public Long create(final CartItem cartItem) {
        return this.cartItemDao.save(cartItem);
    }

    @Override
    public void update(final CartItem cartItem) {
        this.cartItemDao.updateQuantity(cartItem);
    }

    @Override
    public void deleteById(final Long id) {
        this.cartItemDao.deleteById(id);
    }
}
