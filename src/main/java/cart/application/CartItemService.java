package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemDao cartItemDao;

    public CartItemService(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> findByMember(Member member) {
        return cartItemDao.findByMemberId(member.getId());
    }

    public CartItem findByMemberAndProduct(final Member member, final Product product) {
        return cartItemDao.findByMemberIdAndProductId(member.getId(), product.getId());
    }

    public Long add(CartItem cartItem) {
        return cartItemDao.save(cartItem);
    }

    public void updateQuantity(Member member, Long id, int quantity) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (quantity == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(quantity);
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
