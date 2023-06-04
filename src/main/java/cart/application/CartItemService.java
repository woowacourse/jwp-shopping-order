package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemAddRequest;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.response.CartItemResponse;
import cart.exception.NoSuchDataExistException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long addCartItem(final Member member, final CartItemAddRequest cartItemAddRequest) {
        final Product productById = productDao.findProductById(cartItemAddRequest.getProductId())
                .orElseThrow(NoSuchDataExistException::new);
        return cartItemDao.saveCartItem(new CartItem(1, member, productById));
    }

    @Transactional
    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(NoSuchDataExistException::new);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem.getId(), request.getQuantity());
    }

    @Transactional
    public void removeCartItem(final Member member, final Long id) {
        final CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(NoSuchDataExistException::new);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
