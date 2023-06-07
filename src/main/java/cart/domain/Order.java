package cart.domain;

import java.util.List;
import java.util.Objects;

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

    public static Order of(List<CartItem> cartItems, Member member, PriceCalculator priceCalculator) {
        CartItems products = CartItems.of(cartItems, member);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
