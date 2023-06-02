package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDbRepository implements CartItemRepository {
    private final CartItemDao cartItemDao;

    public CartItemDbRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        try {
            return Optional.of(cartItemDao.findById(id));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<CartItem> findByMember(Member member) {
        return cartItemDao.findByMemberId(member.getId());
    }

    @Override
    public Long create(CartItem cartItem) {
        return cartItemDao.save(cartItem);
    }

    @Override
    public void update(CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }
}
