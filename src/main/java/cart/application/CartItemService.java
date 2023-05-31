package cart.application;

import cart.application.repository.CartItemRepository;
import cart.application.repository.ProductRepository;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findAllCartItems(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long createCartItem(Member member, CartItemRequest cartItemRequest) {
        Product product = productRepository.findById(cartItemRequest.getProductId());
        CartItem inserted = cartItemRepository.insert(new CartItem(null, 1, product, member));
        return inserted.getId();
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityRequest request) {
        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 장바구니 품목은 존재하지 않습니다."));

        cartItem.validateIsOwnedBy(member);
        CartItem updated = new CartItem(cartItem.getId(), request.getQuantity(),
                cartItem.getProduct(), cartItem.getMember());
        cartItemRepository.update(updated);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 장바구니 품목은 존재하지 않습니다."));

        cartItem.validateIsOwnedBy(member);
        cartItemRepository.deleteById(id);
    }
}
