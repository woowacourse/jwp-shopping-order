package cart.domain;

import java.util.ArrayList;
import java.util.List;

public class Price {

    private static final int MIN_TOTAL_PRICE_FOR_FREE_SHIPPING = 50000;
    private static final int SHIPPING_FEE = 3000;

    public static int findTotalItemPrice(List<CartItem> cartItems) {
        List<Integer> prices = new ArrayList<>();

        List<Product> products = getProducts(cartItems);
        for (Product product : products) {
            prices.add(product.getPrice());
        }

        return Order.calculatePriceSum(prices);
    }

    public static List<Product> getProducts(List<CartItem> cartItems) {
        List<Product> products = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            products.add(cartItem.getProduct());
        }

        return products;
    }

    public static int findDiscountedTotalItemPrice(List<CartItem> cartItems, int memberDiscount) {
        List<Integer> discountedPrice = new ArrayList<>();

        List<Product> products = getProducts(cartItems);
        for (Product product : products) {
            int price = product.calculateDiscountedPrice(memberDiscount);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

    public static int findShippingFee(int totalItemPrice) {
        if (totalItemPrice >= MIN_TOTAL_PRICE_FOR_FREE_SHIPPING) {
            return 0;
        }
        return SHIPPING_FEE;
    }

    public static int findTotalItemDiscountAmount(List<CartItem> cartItems) {
        List<Integer> discountedPrice = new ArrayList<>();

        List<Product> products = getProducts(cartItems);
        for (Product product : products) {
            int price = product.getPrice() - product.calculateDiscountedPrice();
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

    public static int findTotalMemberDiscountAmount(List<CartItem> cartItems, int memberDiscount) {
        List<Integer> discountedPrice = new ArrayList<>();
        List<Product> products = getProducts(cartItems);
        for (Product product : products) {
            int price = product.getPrice() - product.calculateMemberDiscountedPrice(memberDiscount);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }
}
