package cart.domain.order;

import cart.domain.cartitem.Quantity;

import java.util.Objects;

public class OrderItem {

    private Long id;
    private final Long productId;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final Quantity quantity;

    public OrderItem(Long productId, String name, Long price, String imageUrl, Long quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = new Quantity(quantity);
    }

    public OrderItem(Long id, Long productId, String name, Long price, String imageUrl, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = new Quantity(quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
