package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemCreateRequest;
import cart.dto.response.CartItemResponse;
import cart.exception.CartItemNotFoundException;
import cart.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private static final int INITIAL_CART_ITEM_QUANTITY = 1;

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findAllByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemCreateRequest cartItemCreateRequest) {
        Product product = productDao.findById(cartItemCreateRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
        CartItem cartItem = new CartItem(null, INITIAL_CART_ITEM_QUANTITY, product, member);
        return cartItemDao.save(cartItem);
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("해당 장바구니를 찾을 수 없습니다."));
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("해당 장바구니를 찾을 수 없습니다."));
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
