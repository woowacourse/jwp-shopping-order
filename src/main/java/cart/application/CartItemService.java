package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.exception.notfound.CartItemNotFoundException;
import cart.exception.notfound.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return cartItemDao.findAllByMemberId(member.getId());
    }

    @Transactional
    public Long addCartItem(final Member member, final CartItemRequest request) {
        final Product product = productDao.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));
        final CartItems cartItems = new CartItems(cartItemDao.findAllByMemberId(member.getId()));
        final Optional<CartItem> cartItemOptional = cartItems.find(product);
        if (cartItemOptional.isEmpty()) {
            return cartItemDao.insert(new CartItem(member, product));
        }
        final CartItem cartItem = cartItemOptional.get();
        cartItem.checkOwner(member);
        final CartItem addedCartItem = cartItem.addQuantity();
        cartItemDao.updateQuantity(addedCartItem);

        return cartItem.getId();
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
        final CartItem updatedCartItem = cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(updatedCartItem);
    }

    @Transactional
    public void remove(final Member member, final Long cartItemId) {
        final CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        cartItem.checkOwner(member);
        cartItemDao.delete(cartItemId);
    }
}
