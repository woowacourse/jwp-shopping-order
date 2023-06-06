package cart.domain;

import cart.exception.OrderException;

import java.util.List;

public class CartItems {
    private final List<CartItem> cartProducts;

    public CartItems(List<CartItem> cartProducts) {
        validate(cartProducts);
        this.cartProducts = cartProducts;
    }

    private void validate(List<CartItem> cartProducts) {
        if (cartProducts.isEmpty()) {
            throw new OrderException("주문 상품이 비어있습니다.");
        }
    }

    public int calculatePrice() {
        return cartProducts.stream()
                .mapToInt(it -> it.getProduct().getPrice() * it.getQuantity()).sum();
    }

    public List<CartItem> getCartProducts() {
        return cartProducts;
    }
}
