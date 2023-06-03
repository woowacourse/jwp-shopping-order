package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.RemoveCartItemsRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(final Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final Optional<CartItem> optionalCartItem = cartItemDao.findByMemberIdAndProductId(member.getId(),
            cartItemRequest.getProductId());
        if (optionalCartItem.isPresent()) {
            final CartItem cartItem = optionalCartItem.get();
            final CartItem updatedCartItem = cartItem.addQuantity(cartItemRequest.getQuantity());
            cartItemDao.updateQuantity(updatedCartItem);
            return cartItem.getId();
        }
        return cartItemDao.save(
            new CartItem(cartItemRequest.getQuantity(), productDao.getProductById(cartItemRequest.getProductId()),
                member));
    }

    @Transactional
    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(final Member member, final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    @Transactional
    public void remove(final Member member, final RemoveCartItemsRequest request) {
        final List<CartItem> cartItems = cartItemDao.findAllByIds(request.getCartItemIds());
        for (final CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
        cartItemDao.deleteAll(cartItems);
    }
}
