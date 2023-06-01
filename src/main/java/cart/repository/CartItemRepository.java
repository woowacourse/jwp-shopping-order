package cart.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;

@Repository
public class CartItemRepository {
    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public CartItem findById(Long id) {
        return cartItemDao.findById(id);
    }

    public CartItems findByMember(Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return CartItems.from(cartItems, member);
    }

    public Long add(CartItem cartItem) {
        return cartItemDao.save(cartItem);
    }

    public void updateQuantity(CartItem cartItem) {
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Long id) {
        cartItemDao.deleteById(id);
    }
}
