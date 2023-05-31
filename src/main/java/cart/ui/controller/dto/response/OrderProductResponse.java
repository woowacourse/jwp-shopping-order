package cart.ui.controller.dto.response;

import cart.domain.order.OrderProduct;
import cart.domain.product.Product;

public class OrderProductResponse {

    private Long productId;
    private String name;
    private int price;
    private String imageUrl;
    private int quantity;

    private OrderProductResponse() {
    }

    private OrderProductResponse(Long productId, String name, int price, String imageUrl, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderProductResponse from(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        return new OrderProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                orderProduct.getQuantity()
        );
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
}
