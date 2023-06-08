package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.dto.cart.CartItemProductDto;
import cart.dao.dto.cart.CartItemDto;
import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.exception.notfoundexception.CartItemNotFoundException;
import cart.repository.mapper.CartItemMapper;
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
        CartItemDto cartItemDto = CartItemMapper.toCartItemDto(cartItem);
        return cartItemDao.save(cartItemDto);
    }

    public CartItem findById(long cartItemId) {
        CartItemProductDto foundCartItem = cartItemDao.findById(cartItemId)
            .orElseThrow(CartItemNotFoundException::new);

        return CartItemMapper.toCartItem(foundCartItem);
    }

    public Cart findByMember(Member member) {
        return new Cart(cartItemDao.findByMemberId(member.getId())
            .stream()
            .map(CartItemMapper::toCartItem)
            .collect(Collectors.toList()));
    }

    public void delete(CartItem cartItem) {
        validateCartItemExistence(cartItem.getId());
        cartItemDao.deleteById(cartItem.getId());
    }

    public void updateQuantity(CartItem cartItem) {
        validateCartItemExistence(cartItem.getId());
        cartItemDao.updateQuantity(CartItemMapper.toCartItemDto(cartItem));
    }

    private void validateCartItemExistence(long cartItemId) {
        if (cartItemDao.isNonExistingId(cartItemId)) {
            throw new CartItemNotFoundException();
        }
    }

    public Cart findAllByIds(List<Long> ids) {
        List<CartItem> cartItems = cartItemDao.findAllByIds(ids)
            .stream()
            .map(CartItemMapper::toCartItem)
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
