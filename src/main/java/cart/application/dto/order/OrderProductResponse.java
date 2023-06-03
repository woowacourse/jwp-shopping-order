package cart.application.dto.order;

import cart.domain.Product;
import cart.domain.order.OrderItem;

public class OrderProductResponse {

    private long id;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;

    private OrderProductResponse() {
    }

    public OrderProductResponse(final long id, final String name, final int price, final String imageUrl,
            final int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderProductResponse from(final OrderItem orderItem) {
        Product product = orderItem.getProduct();
        return new OrderProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                orderItem.getQuantity()
        );
    }

    public long getId() {
        return id;
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
}
