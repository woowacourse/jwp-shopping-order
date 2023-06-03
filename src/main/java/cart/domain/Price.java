package cart.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Price {

    private static final int MIN_TOTAL_PRICE_FOR_FREE_SHIPPING = 50000;
    private static final int SHIPPING_FEE = 3000;

    public static int calculateTotalItemPrice(List<CartItem> cartItems) {
        List<Integer> prices = getProducts(cartItems).stream()
                .map(Product::getPrice)
                .collect(Collectors.toList());

        return Order.calculatePriceSum(prices);
    }

    public static List<Product> getProducts(List<CartItem> cartItems) {
        return cartItems.stream()
                .flatMap(cartItem -> Collections.nCopies(cartItem.getQuantity(), cartItem.getProduct()).stream())
                .collect(Collectors.toList());
    }

    public static int calculateDiscountedTotalItemPrice(List<CartItem> cartItems, int memberDiscount) {
        List<Integer> discountedPrice = getProducts(cartItems).stream()
                .map(product -> product.calculateDiscountedPrice(memberDiscount))
                .collect(Collectors.toList());

        return Order.calculatePriceSum(discountedPrice);
    }

    public static int calculateShippingFee(int totalItemPrice) {
        if (totalItemPrice >= MIN_TOTAL_PRICE_FOR_FREE_SHIPPING) {
            return 0;
        }
        return SHIPPING_FEE;
    }
}
