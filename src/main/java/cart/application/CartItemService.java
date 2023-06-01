package cart.application;

import cart.application.dto.request.CartItemQuantityUpdateRequest;
import cart.application.dto.request.CartItemRequest;
import cart.application.dto.response.CartItemResponse;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.persistence.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemService(final CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findAllByMember(final Member member) {
        return cartItemRepository.findCartItemsByMemberId(member.getId()).stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long create(final Member member, final CartItemRequest cartItemRequest) {
        final Product product = cartItemRepository.findProductById(cartItemRequest.getProductId());
        final CartItem cartItem = new CartItem(member, product, cartItemRequest.getQuantity());
        return cartItemRepository.createCartItem(cartItem);
    }

    @Transactional(readOnly = true)
    public void update(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemRepository.findCartItemById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteCartItemById(id);
            return;
        }

        cartItem.updateQuantity(request.getQuantity());
        cartItemRepository.updateCartItemQuantity(cartItem);
    }

    @Transactional(readOnly = true)
    public void delete(final Member member, final Long id) {
        final CartItem cartItem = cartItemRepository.findCartItemById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteCartItemById(id);
    }
}
