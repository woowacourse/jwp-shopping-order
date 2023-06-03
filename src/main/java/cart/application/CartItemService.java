package cart.application;

import cart.application.repository.CartItemRepository;
import cart.application.repository.ProductRepository;
import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Product;
import cart.presentation.dto.request.CartItemQuantityRequest;
import cart.presentation.dto.request.CartItemRequest;
import cart.presentation.dto.response.CartItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartItemService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // TODO: 장바구니 품목 예외 처리(남의 장바구니)
    @Transactional(readOnly = true)
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
        CartItem cartItem = cartItemRepository.findById(id);

        cartItem.validateIsOwnedBy(member);
        CartItem updated = new CartItem(cartItem.getId(), request.getQuantity(),
                cartItem.getProduct(), cartItem.getMember());
        cartItemRepository.update(updated);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);

        cartItem.validateIsOwnedBy(member);
        cartItemRepository.deleteById(id);
    }
}
