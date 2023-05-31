package cart.application;

import cart.dao.CartItemDao;
import cart.domain.cartitem.CartItem;
import cart.domain.Member;
import cart.domain.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemDao cartItemDao;

    public CartItemService(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> findByMember(final Member member) {
        return cartItemDao.findByMemberId(member.getId());
    }

    public CartItem findByMemberAndProduct(final Member member, final Product product) {
        return cartItemDao.findByMemberIdAndProductId(member.getId(), product.getId());
    }

    public Long add(final CartItem cartItem) {
        return cartItemDao.save(cartItem);
    }

    public void updateQuantity(final Member member, final Long id, final int quantity) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (quantity == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(quantity);
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(final Member member, final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
