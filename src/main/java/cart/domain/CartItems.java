package cart.domain;

import java.util.List;

import cart.exception.CartItemException;
import cart.exception.DuplicatedProductCartItemException;

public class CartItems {
    private final List<CartItem> cartItems;

    public CartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer calculatePriceSum() {
        return cartItems.stream()
                .mapToInt(CartItem::calculateTotalPrice)
                .sum();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void validateAllCartItemsBelongsToMember(Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    public void validateExistentCartItems(List<Long> cartItemIds) {
        if (cartItemIds.size() != cartItems.size()) {
            throw new CartItemException("존재하지 않는 cartItemId가 포함되어 있습니다.");
        }
    }

    public void validateContainDuplicatedProduct(Long productId) {
        final boolean isExistedProduct = cartItems.stream()
                .anyMatch(cartItem -> cartItem.isSameProduct(productId));
        if(isExistedProduct) {
            throw new DuplicatedProductCartItemException(
                    "Product is already existed in member's cart; productId =  " + productId);
        }
    }
}
