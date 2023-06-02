package cart.domain;

import java.util.Objects;

public class OrderItem {
    private final Long id;
    private final String name;
    private final int quantity;
    private final String imageUrl;
    private final Money totalPrice;

    public OrderItem(final Long id, final String name, final int quantity, final String imageUrl, final Money totalPrice) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public OrderItem(final String name, final int quantity, final String imageUrl, final Money totalPrice) {
        this(null, name, quantity, imageUrl, totalPrice);
    }

    public static OrderItem from(final CartItem cartItem) {
        final int quantity = cartItem.getQuantity();
        final Product product = cartItem.getProduct();
        final int price = product.getPrice();
        final Money orderPrice = Money.from(price * quantity);
        return new OrderItem(product.getName(), quantity, product.getImageUrl(), orderPrice);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Money getTotalPrice() {
        return this.totalPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final OrderItem orderItem = (OrderItem) o;
        return Objects.equals(this.id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
