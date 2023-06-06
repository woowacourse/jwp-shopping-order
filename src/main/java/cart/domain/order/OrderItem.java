package cart.domain.order;

import cart.domain.CartItem;
import cart.domain.Money;

import java.util.Objects;

public class OrderItem {

    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Money price;
    private final Integer quantity;

    public OrderItem(final Long id, final String name, final String imageUrl, final Money price, final Integer quantity) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem from(final CartItem cartItem) {
        return new OrderItem(null, cartItem.getProduct().getName(), cartItem.getProduct().getImageUrl(), cartItem.getProduct().getPrice(), cartItem.getQuantity());
    }

    public Money priceWithQuantity() {
        return price.mul(quantity);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
