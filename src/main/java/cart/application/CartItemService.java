package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.ProductDao;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        final Long productId = cartItemRequest.getProductId();
        final Optional<CartItem> findCartItem = getCartItem(member, productId);

        if (findCartItem.isPresent()) {
            final CartItem updateCartItem = findCartItem.get();
            updateCartItem.changeQuantity(updateCartItem.getQuantity() + 1);
            cartItemDao.updateQuantity(updateCartItem);
            return updateCartItem.getId();
        }
        return cartItemDao.save(new CartItem(member, productDao.getProductById(cartItemRequest.getProductId())));
    }

    private Optional<CartItem> getCartItem(final Member member, final Long productId) {
        final List<CartItem> memberCart = cartItemDao.findByMemberId(member.getId());
        return memberCart.stream()
                .filter(cartItem -> cartItem.equalsProductId(productId))
                .findFirst();
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    public CartItemResponse findByCartItemId(final Member member, final Long cartItemId) {
        final CartItem findCartItem = cartItemDao.findById(cartItemId);
        findCartItem.checkOwner(member);

        return CartItemResponse.of(findCartItem);
    }
}
