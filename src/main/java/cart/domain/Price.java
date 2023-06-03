package cart.domain;

import java.util.ArrayList;
import java.util.List;

public class Price {

    private static final int MIN_TOTAL_PRICE_FOR_FREE_SHIPPING = 50000;
    private static final int SHIPPING_FEE = 3000;

    public static int calculateTotalItemPrice(List<CartItem> cartItems) {
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
            for (int i = 0; i < cartItem.getQuantity(); i++) {
                products.add(cartItem.getProduct());
            }
        }

        return products;
    }

    public static int calculateDiscountedTotalItemPrice(List<CartItem> cartItems, int memberDiscount) {
        List<Integer> discountedPrice = new ArrayList<>();

        List<Product> products = getProducts(cartItems);
        for (Product product : products) {
            int price = product.calculateDiscountedPrice(memberDiscount);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

    private static List<Product> convertToProducts(List<OrderedItem> orderedItems) {
        ArrayList<Product> products = new ArrayList<>();
        for (OrderedItem orderedItem : orderedItems) {
            products.add(new Product(orderedItem.getId(), orderedItem.getName(), orderedItem.getPrice()
                    , orderedItem.getImageUrl(), orderedItem.getDiscountRate()));
        }
        return products;
    }

    public static int calculateShippingFee(int totalItemPrice) {
        if (totalItemPrice >= MIN_TOTAL_PRICE_FOR_FREE_SHIPPING) {
            return 0;
        }
        return SHIPPING_FEE;
    }

    public static int calculateTotalItemDiscountAmount(List<CartItem> cartItems) {
        List<Integer> discountedPrice = new ArrayList<>();

        List<Product> products = getProducts(cartItems);
        for (Product product : products) {
            int price = product.calculateDiscountedPrice();
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }

    public static int calculateTotalMemberDiscountAmount(List<CartItem> cartItems, int memberDiscount) {
        List<Integer> discountedPrice = new ArrayList<>();
        List<Product> products = getProducts(cartItems);
        for (Product product : products) {
            int price = product.calculateMemberDiscountedPrice(memberDiscount);
            discountedPrice.add(price);
        }

        return Order.calculatePriceSum(discountedPrice);
    }
}
