package cart.cartitem.application;

import cart.cartitem.dao.CartItemDao;
import cart.cartitem.domain.CartItem;
import cart.member.domain.Member;
import cart.product.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        final Member member = cartItem.getMember();
        final Product product = cartItem.getProduct();
        final Optional<CartItem> nullableCartItem = Optional.ofNullable(
                cartItemDao.findByMemberIdAndProductId(member.getId(), product.getId()));

        if (nullableCartItem.isPresent()) {
            return addQuantity(nullableCartItem.get());
        }
        return cartItemDao.save(cartItem);
    }

    private Long addQuantity(final CartItem cartItem) {
        cartItem.addQuantity(cartItem.getQuantity());
        cartItemDao.updateQuantity(cartItem);
        return cartItem.getId();
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
