package cart.domain.orderitem;

import cart.domain.order.Order;

public class OrderItem {

    private Long id;
    private final Order order;
    private final Long productId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;

    public OrderItem(final Order order, final Long productId,
                     final String name, final int price,
                     final String imageUrl, final int quantity) {
        this.order = order;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public OrderItem(final Long id, final Order order,
                     final Long productId, final String name,
                     final int price, final String imageUrl,
                     final int quantity) {
        this.id = id;
        this.productId = productId;
        this.order = order;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
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

    public Long getProductId() {
        return productId;
    }
}
