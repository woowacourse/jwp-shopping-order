package cart.cartitem.application;

import cart.cartitem.dao.CartItemDao;
import cart.cartitem.domain.CartItem;
import cart.cartitem.exception.NotFoundCartItemException;
import cart.member.domain.Member;
import cart.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private static final int NOT_EXIST_CART_ITEM = 0;
    private final CartItemDao cartItemDao;

    public CartItemService(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Transactional(readOnly = true)
    public List<CartItem> findByMember(final Member member) {
        return cartItemDao.findByMemberId(member.getId());
    }

    @Transactional(readOnly = true)
    public CartItem findByMemberAndProduct(final Member member, final Product product) {
        return cartItemDao.findByMemberIdAndProductId(member.getId(), product.getId());
    }

    @Transactional
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
        validateExistCartItem(cartItem.getId());

        cartItem.addQuantity(cartItem.getQuantity());
        cartItemDao.updateQuantity(cartItem);
        return cartItem.getId();
    }

    @Transactional
    public void updateQuantity(final Member member, final Long id, final int quantity) {
        validateExistCartItem(id);

        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (quantity == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(quantity);
        cartItemDao.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(final Member member, final Long id) {
        validateExistCartItem(id);

        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    private void validateExistCartItem(final Long id) {
        if (cartItemDao.countById(id) == NOT_EXIST_CART_ITEM) {
            throw new NotFoundCartItemException();
        }
    }
}
