package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
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
        return cartItemDao.save(new CartItem(member, productDao.getProductById(cartItemRequest.getProductId())));
    }

    @Transactional
    public void updateQuantity(final Member member, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(cartItemId);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(cartItemId);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(final Member member, final Long cartItemId) {
        final CartItem cartItem = cartItemDao.findById(cartItemId);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(cartItemId);
    }
}
