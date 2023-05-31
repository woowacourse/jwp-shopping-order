package cart.dto;

import cart.domain.OrderInfo;
import cart.domain.Product;

public class OrderInfoResponse {

    private final Long productId;
    private final int price;
    private final String name;
    private final String imageUrl;
    private final int quantity;

    private OrderInfoResponse(Long productId, int price, String name, String imageUrl, int quantity) {
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderInfoResponse from(OrderInfo orderInfo) {
        Product product = orderInfo.getProduct();
        return new OrderInfoResponse(product.getId(), product.getPrice(), product.getName(), product.getImageUrl(), orderInfo.getQuantity().intValue());
    }

    public Long getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
