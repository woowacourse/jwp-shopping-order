package cart.application;

import cart.domain.CartItem;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import cart.repository.CartItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private static final int QUANTITY_MAX = 10;

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartItemService(final CartItemRepository cartItemRepository, final ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public List<CartItemResponse> findByMember(final long memberId) {
        final List<CartItem> cartItems = cartItemRepository.findByMember(memberId);
        return CartItemResponse.from(cartItems);
    }

    public Long add(final long memberId, final CartItemRequest cartItemRequest) {
        final Product product = productService.checkId(cartItemRequest.getProductId());
        return cartItemRepository.add(memberId, new CartItem(product));
    }

    public void updateQuantity(final long memberId, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = checkId(memberId, id);
        final int quantity = request.getQuantity();
        validateQuantityMax(quantity);
        if (quantity == 0) {
            cartItemRepository.removeById(id);
            return;
        }
        cartItemRepository.update(memberId, cartItem.changeQuantity(quantity));
    }

    private void validateQuantityMax(final int quantity) {
        if (quantity > QUANTITY_MAX) {
            throw new CartItemException.IllegalQuantity(quantity, QUANTITY_MAX);
        }
    }

    public void remove(final long memberId, final List<Long> ids) {
        ids.forEach(id -> checkId(memberId, id));
        cartItemRepository.removeAllById(ids);
    }

    public CartItem checkId(final long memberId, final long cartItemId) {
        return cartItemRepository.findByIdForMember(memberId, cartItemId)
                .orElseThrow(() -> new CartItemException.IllegalId(cartItemId));
    }
}
