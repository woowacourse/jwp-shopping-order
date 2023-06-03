package cart.domain.cartitem;

import cart.domain.Member;
import cart.ui.order.dto.CreateOrderItemDto;

import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int calculateTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::calculatePriceOfQuantity)
                .sum();
    }

    public void validate(final List<CreateOrderItemDto> createOrderItemDtos, final Long memberId) {
        for (CreateOrderItemDto createOrderItemDto : createOrderItemDtos) {
            cartItems.forEach(cartItem ->
                    cartItem.validate(
                            createOrderItemDto.getProductId(),
                            memberId,
                            createOrderItemDto.getQuantity())
            );
        }
    }

    public boolean isNotOwnedMember(final Member member) {
        return cartItems.stream()
                .anyMatch(cartItem -> cartItem.isNotOwnedByMember(member));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
