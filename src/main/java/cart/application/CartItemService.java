package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import cart.exception.CartItemNotFoundException;
import cart.exception.MemberNotFoundException;
import cart.exception.ProductNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao, MemberDao memberDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
    }

    public List<CartItemResponse> findAllByMember(Long memberId) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(memberId);
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long add(Long memberId, CartItemRequest cartItemRequest) {
        Product product = productDao.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다."));
        CartItem cartItem = CartItem.builder()
                .member(member)
                .product(product)
                .quantity(1)
                .build();
        return cartItemDao.save(cartItem);
    }

    public void updateQuantity(Long memberId, Long cartItemId, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("해당 장바구니를 찾을 수 없습니다."));
        cartItem.checkOwner(memberId);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(cartItemId);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Long memberId, Long cartItemId) {
        CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("해당 장바구니를 찾을 수 없습니다."));
        cartItem.checkOwner(memberId);

        cartItemDao.deleteById(cartItemId);
    }
}
