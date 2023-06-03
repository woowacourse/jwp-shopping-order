package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import cart.exception.ProductException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void removeAfterOrder(final List<Long> cartItemIds) {
        cartItemIds.forEach(this.cartItemRepository::deleteById);
    }
}
