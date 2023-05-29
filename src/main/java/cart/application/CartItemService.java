package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.exception.CartItemNotFoundException;
import cart.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> getCartItemsByMember(final Member member) {
        return cartItemDao.findByMemberId(member.getId());
    }

    @Transactional
    public Long addCartItems(final Member member, final CartItemRequest cartItemRequest) {
        final Product product = productDao.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(cartItemRequest.getProductId()));
        return cartItemDao.insert(new CartItem(member, product));
    }

    @Transactional
    public void updateQuantity(final Member member, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        cartItem.checkOwner(member);
        if (request.getQuantity() == 0) {
            cartItemDao.delete(cartItemId);
            return;
        }
        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(final Member member, final Long cartItemId) {
        final CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        cartItem.checkOwner(member);

        cartItemDao.delete(cartItemId);
    }
}
