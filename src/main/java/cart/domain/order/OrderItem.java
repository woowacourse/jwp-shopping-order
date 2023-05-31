package cart.domain.order;

import cart.domain.CartItem;
import cart.domain.Money;

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
