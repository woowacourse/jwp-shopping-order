package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.dto.CartItemProductDto;
import cart.dao.entity.CartItemEntity;
import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.exception.CartItemNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public long save(CartItem cartItem) {
        CartItemEntity cartItemEntity = CartItemEntity.fromDomain(cartItem);
        return cartItemDao.save(cartItemEntity);
    }

    public CartItem findById(long cartItemId) {
        CartItemProductDto foundCartItem = cartItemDao.findById(cartItemId)
            .orElseThrow(CartItemNotFoundException::new);

        return foundCartItem.toDomain();
    }

    public Cart findByMember(Member member) {
        return new Cart(cartItemDao.findByMemberId(member.getId())
            .stream()
            .map(CartItemProductDto::toDomain)
            .collect(Collectors.toList()));
    }

    public void delete(CartItem cartItem) {
        validateCartItemExistence(cartItem.getId());
        cartItemDao.deleteById(cartItem.getId());
    }

    public void updateQuantity(CartItem cartItem) {
        validateCartItemExistence(cartItem.getId());
        cartItemDao.updateQuantity(CartItemEntity.fromDomain(cartItem));
    }

    private void validateCartItemExistence(long cartItemId) {
        if (cartItemDao.isNonExistingId(cartItemId)) {
            throw new CartItemNotFoundException();
        }
    }

    public Cart findAllByIds(List<Long> ids) {
        List<CartItem> cartItems = cartItemDao.findAllByIds(ids)
            .stream()
            .map(CartItemProductDto::toDomain)
            .collect(Collectors.toList());
        validateAllCartItemsFound(ids, cartItems);
        return new Cart(cartItems);
    }

    private void validateAllCartItemsFound(List<Long> ids, List<CartItem> cartItems) {
        if (ids.size() != cartItems.size()) {
            throw new CartItemNotFoundException();
        }
    }

    public void removeAllByIds(List<Long> ids) {
        cartItemDao.removeAllByIds(ids);
    }
}
