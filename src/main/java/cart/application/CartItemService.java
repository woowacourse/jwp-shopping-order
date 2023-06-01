package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import cart.exception.CartItemException.DuplicatedCartItem;
import cart.exception.CartItemException.InvalidCartItem;
import cart.exception.ProductException.InvalidProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        Collections.reverse(cartItems);
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productDao.getProductById(cartItemRequest.getProductId())
                .orElseThrow(InvalidProduct::new);
        if (cartItemDao.countByMemberIdAndProductId(member.getId(), cartItemRequest.getProductId()) != 0) {
            throw new DuplicatedCartItem();
        }
        return cartItemDao.save(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = findCartItem(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }
        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    private CartItem findCartItem(final Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        if (cartItem == null) {
            throw new InvalidCartItem();
        }
        return cartItem;
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = findCartItem(id);
        cartItem.checkOwner(member);
        cartItemDao.deleteById(id);
    }
}
