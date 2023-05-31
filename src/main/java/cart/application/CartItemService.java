package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.request.CartItemCreateRequest;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.response.CartItemResponse;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(
            final ProductRepository productRepository,
            final CartItemRepository cartItemRepository
    ) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemCreateRequest request) {
        return cartItemRepository.save(
                new CartItem(
                        member,
                        productRepository.getProductById(request.getProductId()),
                        request.getQuantity()
                )
        );
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        final CartItem updatedCartItem = cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(updatedCartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }
}
