package cart.service;

import cart.controller.dto.request.CartItemQuantityUpdateRequest;
import cart.controller.dto.request.CartItemRequest;
import cart.controller.dto.response.CartItemResponse;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.exception.NotOwnerException;
import cart.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private static final int CART_ITEM_REMOVABLE_QUANTITY = 0;
    private static final int CART_ITEM_INITIAL_QUANTITY = 1;

    private final CartItemRepository cartItemRepository;

    public CartItemService(final CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItemResponse> findByMember(final Member member) {
        final List<CartItem> cartItems = cartItemRepository.findByMember(member);
        return cartItems.stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public long save(final Member member, final CartItemRequest cartItemRequest) {
        return cartItemRepository.save(member, cartItemRequest.getProductId(), CART_ITEM_INITIAL_QUANTITY);
    }

    public void updateQuantity(final Member member, final long id, final CartItemQuantityUpdateRequest request) {
        checkOwner(member, id);
        cartItemRepository.updateQuantity(id, request.getQuantity());
        if (request.getQuantity() == CART_ITEM_REMOVABLE_QUANTITY) {
            cartItemRepository.delete(id);
        }
    }

    public void remove(Member member, long id) {
        checkOwner(member, id);
        cartItemRepository.delete(id);
    }

    private void checkOwner(final Member member, final long id) {
        final long cartItemsMemberId = cartItemRepository.findMemberIdById(id);
        if (member.isIdEquals(cartItemsMemberId)) {
            return;
        }
        throw new NotOwnerException();
    }
}
