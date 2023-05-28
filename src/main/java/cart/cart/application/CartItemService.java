package cart.cart.application;

import cart.cart.CartItem;
import cart.cart.presentation.dto.CartItemQuantityUpdateRequest;
import cart.cart.presentation.dto.CartItemRequest;
import cart.cart.presentation.dto.CartItemResponse;
import cart.member.Member;
import cart.product.application.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartItemService(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartRepository.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartRepository.save(new CartItem(member, productRepository.getProductById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartRepository.findById(id);
        cartItem.checkOwner(member);

        cartRepository.deleteById(id);
    }
}
