package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
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
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemDao.save(new CartItem(null, 1,
                productDao.getProductById(cartItemRequest.getProductId()), member));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityRequest request) {
        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.validateIsOwnedBy(member);
        CartItem updated = new CartItem(cartItem.getId(), request.getQuantity(),
                cartItem.getProduct(), cartItem.getMember());
        cartItemDao.updateQuantity(updated);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.validateIsOwnedBy(member);
        cartItemDao.deleteById(id);
    }
}
