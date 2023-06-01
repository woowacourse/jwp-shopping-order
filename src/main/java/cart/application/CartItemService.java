package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.cartitem.CartItemResponse;
import java.util.Optional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        CartItems cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.getItems().stream().map(CartItemResponse::of).collect(Collectors.toList());
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
        final CartItems memberCart = cartItemDao.findByMemberId(member.getId());
        return memberCart.getItems().stream()
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
}
