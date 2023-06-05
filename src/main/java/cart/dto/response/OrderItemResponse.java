package cart.dto.response;

import cart.domain.OrderItem;
import cart.domain.Product;

public class OrderItemResponse {

    private Long productId;
    private String name;
    private String imageUrl;
    private int price;
    private int quantity;

    private OrderItemResponse() {
    }

    private OrderItemResponse(
            Long productId,
            String name,
            String imageUrl,
            int price,
            int quantity
    ) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        Product orderProduct = orderItem.getProduct();

        return new OrderItemResponse(
                orderProduct.getId(),
                orderProduct.getName(),
                orderProduct.getImageUrl(),
                orderProduct.getPrice(),
                orderItem.getQuantity()
        );
    }

    public Long getProductId() {
        return productId;
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
