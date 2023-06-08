package cart.cartitem.application;

import cart.cartitem.CartItem;
import cart.cartitem.presentation.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.presentation.dto.CartItemRequest;
import cart.product.application.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Long add(Long memberId, CartItemRequest cartItemRequest) {
        final var product = productRepository.getProductById(cartItemRequest.getProductId());
        return cartItemRepository.save(new CartItem(product.getName(), product.getPrice(), 1, product.getImageUrl(), product.getId(), memberId));
    }

    public void updateQuantity(Long memberId, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(memberId);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Long memberId, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(memberId);

        cartItemRepository.deleteById(id);
    }
}
