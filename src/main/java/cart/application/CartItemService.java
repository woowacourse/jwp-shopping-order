package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.ProductRepository;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemService(final CartItemRepository cartItemRepository, final ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public CartItemResponse findByMemberAndProductId(final Member member, final Long productId) {
        final CartItem cartItem = cartItemRepository.findByMemberIdAndProductId(member.getId(), productId);
        return CartItemResponse.of(cartItem);
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemRepository.save(new CartItem(member, productRepository.findById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findCartItemById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteCartItemById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findCartItemById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteCartItemById(id);
    }
}
