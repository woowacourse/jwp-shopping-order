package cart.domain.cartitem.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cart.domain.product.domain.Product;
import cart.global.exception.CartItemNotFoundException;

public class CartItems {

    private static final int CONTAIN_PRODUCT_COUNT = 1;
    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = new ArrayList<>(cartItems);
    }

    public boolean isContainProduct(Product product) {
        long count = cartItems.stream()
                .filter(cartItem -> cartItem.isSameProduct(product))
                .count();

        return count == CONTAIN_PRODUCT_COUNT;
    }

    public CartItem findCartItemByProduct(Product product) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.isSameProduct(product))
                .findAny()
                .orElseThrow(() -> new CartItemNotFoundException("상품에 해당하는 장바구니 상품을 찾을 수 없습니다."));
    }

    public CartItems getCartItemsByCartItemIds(List<Long> cartItemIds) {
        List<CartItem> cartItems = cartItemIds.stream()
                .map(this::getCartItemById)
                .collect(Collectors.toUnmodifiableList());
        return new CartItems(cartItems);
    }

    public CartItem getCartItemById(Long cartItemId) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.isSameId(cartItemId))
                .findAny()
                .orElseThrow(() -> new CartItemNotFoundException("장바구니 상품에 없는 상품입니다."));
    }

    public int getTotalPrice() {
        return cartItems.stream()
                .map(CartItem::calculateAllPrice)
                .reduce(0, Integer::sum);
    }

    public List<Long> getCartItemIds() {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<CartItem> getCartItems() {
        return List.copyOf(cartItems);
    }
}
