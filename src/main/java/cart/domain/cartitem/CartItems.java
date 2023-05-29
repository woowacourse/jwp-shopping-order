package cart.domain.cartitem;

import cart.domain.orderproduct.Order;
import cart.domain.orderproduct.OrderProduct;
import cart.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CartItems {

    public static final int SHIPPING_FEE = 3000;

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int calculateTotalPrice() {
        int totalPrice = cartItems.stream()
                .mapToInt(cartitem -> cartitem.getProductPrice() * cartitem.getQuantityValue())
                .sum();
        if (totalPrice >= 50_000) {
            totalPrice -= SHIPPING_FEE;
        }
        return totalPrice;
    }

    public List<OrderProduct> toOrderProducts(final Order order, final List<Product> products) {
        return IntStream.range(0, products.size())
                .mapToObj(index -> new OrderProduct(
                        order,
                        products.get(index),
                        products.get(index).getProductName(),
                        products.get(index).getProductPrice(),
                        products.get(index).getProductImageUrl(),
                        cartItems.get(index).getQuantity())
                ).collect(Collectors.toList());
    }

    public List<Long> getProductIds() {
        return cartItems.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toList());
    }

    public List<Long> getCartItemIds() {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
