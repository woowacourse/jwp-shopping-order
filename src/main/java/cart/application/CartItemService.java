package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemResponse> findByMember(Member member) {
        final CartItems cartItems = cartItemRepository.findByMember(member);
        return cartItems.getCartItems().stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long add(Member member, CartItemRequest cartItemRequest) {
        final CartItems cartItems = cartItemRepository.findByMember(member);
        cartItems.validateContainDuplicatedProduct(cartItemRequest.getProductId());
        final Product product = productRepository.findById(cartItemRequest.getProductId());
        final CartItem cartItem = new CartItem(product, member);
        return cartItemRepository.add(cartItem);
    }

    @Transactional
    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);
        if (request.getQuantity() == 0) {
            cartItemRepository.remove(id);
            return;
        }
        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    @Transactional
    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.remove(id);
    }
}
