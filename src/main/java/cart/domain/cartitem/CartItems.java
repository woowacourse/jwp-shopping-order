package cart.domain.cartitem;

import java.util.ArrayList;
import java.util.List;

import cart.domain.product.Product;
import cart.exception.CartItemNotFoundException;

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
}
