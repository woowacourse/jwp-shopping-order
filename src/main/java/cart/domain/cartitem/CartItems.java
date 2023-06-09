package cart.domain.cartitem;

import cart.application.service.order.dto.CreateOrderItemDto;

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
        final boolean isValid = cartItems.stream()
                .allMatch(cartItem -> createOrderItemDtos.stream()
                        .anyMatch(createOrderItemDto -> {
                            final boolean isValidProduct = cartItem.validateProduct(createOrderItemDto.getProductId(), memberId);
                            final boolean isValidQuantity = cartItem.validateQuantity(createOrderItemDto.getQuantity());
                            return isValidProduct && isValidQuantity;
                        }));

        if (!isValid) {
            throw new IllegalArgumentException("장바구니 정보가 일치하지 않습니다.");
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
