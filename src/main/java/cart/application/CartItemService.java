package cart.application;

import static java.util.stream.Collectors.toList;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemDuplicateException;
import cart.exception.CartItemException;
import cart.exception.ProductNotFound;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public CartItemService(CartItemDao cartItemDao, ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productDao.findById(cartItemRequest.getProductId())
                .orElseThrow(ProductNotFound::new);
        validateDuplicateCartItem(member.getId(), cartItemRequest.getProductId());
        CartItem cartItem = new CartItem(product, member.getId());
        return cartItemDao.save(cartItem);
    }

    private void validateDuplicateCartItem(Long memberId, Long productId) {
        if (cartItemDao.findByMemberIdAndProductId(memberId, productId).isPresent()) {
            throw new CartItemDuplicateException();
        }
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(toList());
    }

    private CartItem findCartItemById(Long id) {
        return cartItemDao.findById(id)
                .orElseThrow(CartItemException::new);
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = findCartItemById(id);
        cartItem.checkOwner(member.getId());

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = findCartItemById(id);
        cartItem.checkOwner(member.getId());
        cartItemDao.deleteById(id);
    }
}
