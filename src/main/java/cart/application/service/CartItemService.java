package cart.application.service;

import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Product;
import cart.ui.dto.request.CartItemQuantityUpdateRequest;
import cart.ui.dto.request.CartItemRequest;
import cart.ui.dto.response.CartItemResponse;
import cart.application.repository.CartItemRepository;
import cart.application.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemService(final CartItemRepository cartItemRepository, final ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemResponse> getMemberCartItems(final Member member) {
        final List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long createCartItem(final Member member, final CartItemRequest cartItemRequest) {
        final Product product = productRepository.findById(cartItemRequest.getProductId());
        final CartItem cartItem = cartItemRepository.save(new CartItem(product, member));
        return cartItem.getId();
    }

    public void updateQuantity(final Member member, final Long cartItemId, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId);
        cartItem.validateOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemId);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void removeCartItem(final Member member, final Long id) {
        final CartItem cartItem = cartItemRepository.findById(id);
        cartItem.validateOwner(member);

        cartItemRepository.deleteById(id);
    }
}
