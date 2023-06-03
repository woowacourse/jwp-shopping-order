package cart.application.dto.order;

import cart.domain.Product;
import cart.domain.order.OrderItem;

public class OrderDetailProductResponse {

    private final long id;
    private final String name;
    private final String imageUrl;
    private final int price;
    private final int quantity;

    public OrderDetailProductResponse(final long id, final String name, final String imageUrl, final int price,
            final int quantity) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderDetailProductResponse from(final OrderItem orderItem) {
        Product product = orderItem.getProduct();
        return new OrderDetailProductResponse(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                orderItem.getQuantity()
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
