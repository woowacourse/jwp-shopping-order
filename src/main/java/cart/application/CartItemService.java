package cart.application;

import cart.domain.cart.CartItem;
import cart.domain.cart.CartItemRepository;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.dto.cart.CartItemResponse;
import cart.exception.CartItemException;
import cart.exception.ProductException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(final ProductRepository productRepository, final CartItemRepository cartItemRepository) {
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(final Member member) {
        final List<CartItem> cartItems = this.cartItemRepository.findByMember(member);
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final Product product = this.productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ProductException.NotFound(cartItemRequest.getProductId()));

        return this.cartItemRepository.create(new CartItem(member, product));
    }

    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = this.cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemException.NotFound(id));
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            this.cartItemRepository.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        this.cartItemRepository.update(cartItem);
    }

    public void remove(final Member member, final Long id) {
        final CartItem cartItem = this.cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemException.NotFound(id));
        cartItem.checkOwner(member);
        this.cartItemRepository.deleteById(id);
    }
}
