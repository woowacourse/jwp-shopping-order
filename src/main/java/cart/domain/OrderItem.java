package cart.domain;

import java.util.Objects;

public class OrderItem {

    private Long id;
    private OrderHistory orderHistory;
    private Long productId;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;

    public OrderItem(final OrderHistory orderHistory, final Long productId, final String name, final int price, final String imageUrl,
                     final int quantity) {
        this(null, orderHistory, productId, name, price, imageUrl, quantity);
    }

    public OrderItem(final Long id, OrderHistory orderHistory, final Long productId, final String name, final int price, final String imageUrl,
                     final int quantity) {
        this.id = id;
        this.orderHistory = orderHistory;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public OrderHistory getOrderHistory() {
        return orderHistory;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return price == orderItem.price && quantity == orderItem.quantity && Objects.equals(id, orderItem.id)
                && Objects.equals(orderHistory, orderItem.orderHistory) && Objects.equals(productId,
                orderItem.productId) && Objects.equals(name, orderItem.name) && Objects.equals(imageUrl,
                orderItem.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderHistory, productId, name, price, imageUrl, quantity);
    }
}
