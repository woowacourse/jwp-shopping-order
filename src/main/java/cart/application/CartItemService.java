package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
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
    private static final int ZERO_QUANTITY = 0;
    private static final boolean NOT_CHECKED = false;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public CartItemResponse findById(Long cartItemId) {
        final CartItem cartItem = cartItemDao.findById(cartItemId);
        return CartItemResponse.of(cartItem);
    }

    public Long add(Member member, CartItemAddRequest cartItemAddRequest) {
        return cartItemDao.save(new CartItem(member, productDao.getProductById(cartItemAddRequest.getProductId())));
    }

    public CartItemUpdateResponse updateQuantity(Member member, Long id, CartItemUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == ZERO_QUANTITY) {
            cartItemDao.deleteById(id);
            return new CartItemUpdateResponse(ZERO_QUANTITY, NOT_CHECKED);
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItem.changeChecked(request.isChecked());
        cartItemDao.update(cartItem);
        return new CartItemUpdateResponse(cartItem.getQuantity(), cartItem.isChecked());
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
