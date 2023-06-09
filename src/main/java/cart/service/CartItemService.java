package cart.service;

import cart.auth.Authenticate;
import cart.auth.Credentials;
import cart.controller.dto.CartItemQuantityUpdateRequest;
import cart.controller.dto.CartItemRequest;
import cart.controller.dto.CartItemResponse;
import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
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

    public List<CartItemResponse> findByMember(@Authenticate Credentials credentials) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(credentials.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(@Authenticate Credentials credentials, CartItemRequest cartItemRequest) {
        final Member member = new Member(credentials.getId(), credentials.getEmail(), credentials.getPassword());
        return cartItemDao.save(new CartItem(member, productDao.getProductById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(@Authenticate Credentials credentials, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        checkOwner(credentials, cartItem);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(@Authenticate Credentials credentials, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        checkOwner(credentials, cartItem);

        cartItemDao.deleteById(id);
    }

    private void checkOwner(final Credentials credentials, final CartItem cartItem) {
        final Member member = new Member(credentials.getId(), credentials.getEmail(), credentials.getPassword());
        cartItem.checkOwner(member);
    }
}
