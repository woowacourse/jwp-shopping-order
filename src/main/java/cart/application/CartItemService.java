package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemAddRequest;
import cart.dto.request.CartItemUpdateRequest;
import cart.dto.response.CartItemResponse;
import cart.dto.response.CartItemUpdateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findAllByMember(final Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public CartItemResponse findById(final Long cartItemId) {
        CartItem cartItem = cartItemDao.findById(cartItemId);
        return CartItemResponse.of(cartItem);
    }

    public Long add(final Member member, final CartItemAddRequest cartItemAddRequest) {
        Product newProduct = productDao.findProductById(cartItemAddRequest.getProductId());
        CartItem cartItem = new CartItem(member, newProduct);
        return cartItemDao.save(cartItem);
    }

    public CartItemUpdateResponse updateQuantity(
            final Member member,
            final Long id,
            final CartItemUpdateRequest request
    ) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return new CartItemUpdateResponse(0, false);
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItem.changeChecked(request.isChecked());
        cartItemDao.update(cartItem);
        return new CartItemUpdateResponse(cartItem.getQuantity(), cartItem.isChecked());
    }

    public void remove(final Member member, final Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
