package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

import cart.exception.CartItemException;
import cart.exception.DuplicatedProductCartItemException;

public class CartItems {
    private final List<CartItem> cartItems;

    public CartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static CartItems from(List<CartItem> cartItems, Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
        return new CartItems(cartItems);
    }

    public Integer calculatePriceSum() {
        return cartItems.stream()
                .mapToInt(CartItem::calculateTotalPrice)
                .sum();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void validateContainDuplicatedProduct(Long productId) {
        final boolean isExistedProduct = cartItems.stream()
                .anyMatch(cartItem -> cartItem.isSameProduct(productId));
        if (isExistedProduct) {
            throw new DuplicatedProductCartItemException(
                    "Product is already existed in member's cart; productId =  " + productId);
        }
    }

    public List<Long> getProductIds() {
        return cartItems.stream()
                .map(cartItem -> cartItem.getProductId())
                .collect(Collectors.toUnmodifiableList());
    }
}
