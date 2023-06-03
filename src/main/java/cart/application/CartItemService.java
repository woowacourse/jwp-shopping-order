package cart.application;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.Quantity;
import cart.domain.member.Member;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.request.CartItemResponse;
import cart.repository.CartRepository;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartItemService(final ProductRepository productRepository, final CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        Cart cart = cartRepository.findByMember(member);
        return cart.getCartItems().stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartRepository.save(
                new CartItem(member, productRepository.getProductById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(new Quantity(request.getQuantity()));
        cartRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        cartRepository.deleteById(id);
    }
}
