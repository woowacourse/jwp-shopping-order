package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.exception.CartItemException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Long save(Member member, CartItem cartItem) {
        if (cartItemDao.isExistBy(member.getId(), cartItem.getProduct().getId())) {
            throw new CartItemException.AlreadyExist(member, cartItem.getProduct().getId());
        }

        return cartItemDao.save(cartItem);
    }

    public List<CartItem> findByMemberId(Member member) {
        return cartItemDao.findByMemberId(member.getId());
    }

    public Optional<CartItem> findById(Long id) {
        return cartItemDao.findById(id);
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }

    public void deleteById(Long id) {
        cartItemDao.deleteById(id);
    }
}
