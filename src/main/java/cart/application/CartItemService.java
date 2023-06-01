package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.exception.CartItemException;
import cart.repository.CartItemRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private static final int QUANTITY_MAX = 10;

    private final CartItemRepository cartItemRepository;

    public CartItemService(final CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(final Member member) {
        final List<CartItem> cartItems = cartItemRepository.findByMember(member);
        return CartItemResponse.from(cartItems);
    }

    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final CartItem cartItem = cartItemRepository.create(member, cartItemRequest.getProductId());
        return cartItemRepository.add(cartItem);
    }

    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = checkMember(member, id);
        // TODO 원시값 포장
        final int quantity = request.getQuantity();
        validateQuantityMax(quantity);
        if (quantity == 0) {
            cartItemRepository.removeById(id);
            return;
        }
        cartItemRepository.update(cartItem.changeQuantity(quantity));
    }

    private void validateQuantityMax(final int quantity) {
        if (quantity > QUANTITY_MAX) {
            throw new CartItemException.IllegalQuantity(quantity, QUANTITY_MAX);
        }
    }

    public void remove(final Member member, final List<Long> ids) {
        ids.forEach(id -> checkMember(member, id));
        cartItemRepository.removeAllById(ids);
    }

    public CartItem checkMember(final Member member, final Long cartItemId) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException.IllegalId(cartItemId));
        if (!Objects.equals(member.getId(), cartItem.getMember().getId())) {
            throw new CartItemException.IllegalMember(cartItem, member);
        }
        return cartItem;
    }
}
