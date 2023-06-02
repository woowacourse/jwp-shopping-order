package cart.cartitem.application;

import cart.cartitem.CartItem;
import cart.cartitem.presentation.dto.CartItemQuantityUpdateRequest;
import cart.cartitem.presentation.dto.CartItemRequest;
import cart.member.Member;
import cart.product.Product;
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

    public Long add(Member member, CartItemRequest cartItemRequest) {
        final var product = productRepository.getProductById(cartItemRequest.getProductId());
        return cartItemRepository.save(new CartItem(product.getName(), product.getPrice(), 1, product.getImageUrl(), product.getId(), member.getId()));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemRepository.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemRepository.findById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteById(id);
    }
}
