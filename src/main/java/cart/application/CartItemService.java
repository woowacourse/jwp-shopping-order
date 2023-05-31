package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.repository.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemService(final CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public CartItemResponse addCartItem(final Member member, final CartItemRequest cartItemRequest) {
        final Long productId = cartItemRequest.getProductId();
        final CartItem cartItem = cartItemRepository.addCartItem(member, productId);
        return CartItemResponse.of(cartItem);
    }

    public List<CartItemResponse> findByMember(final Member member) {
        return cartItemRepository.findByMember(member)
                .stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        cartItemRepository.updateQuantity(member, id, request.getQuantity());
    }

    public void deleteCartItem(final Member member, final Long id) {
        cartItemRepository.deleteCartItem(member, id);
    }
}
