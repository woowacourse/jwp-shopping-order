package cart.domain;

import java.util.List;

import cart.domain.price.PriceCalculator;

public class Order {
    private final Long id;
    private final Integer price;
    private final Member member;
    private final CartItems orderedItems;

    public Order(Long id, Integer price, Member member, CartItems orderedItems) {
        this.id = id;
        this.price = price;
        this.member = member;
        this.orderedItems = orderedItems;
    }

    public Order(Integer price, Member member, CartItems orderedItems) {
        this(null, price, member, orderedItems);
    }

    public static Order from(List<CartItem> cartItems, Member member, PriceCalculator priceCalculator) {
        CartItems products = CartItems.from(cartItems, member);
        final int totalPrice = products.calculatePriceSum();
        final int finalPrice = priceCalculator.calculateFinalPrice(totalPrice, member);
        return new Order(finalPrice, member, products);
    }

    public Long getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public Member getMember() {
        return member;
    }

    public List<CartItem> getOrderedItems() {
        return orderedItems.getCartItems();
    }

    public List<Long> getOrderedItemIds() {
        return orderedItems.getProductIds();
    }
}
