package cart.application.service.order.dto;

import cart.domain.order.OrderItem;

public class OrderedItemResult {

    private final long id;
    private final String productName;
    private final int productPrice;
    private final int productQuantity;
    private final String imageUrl;

    public OrderedItemResult(long id, String productName, int productPrice, int productQuantity, String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.imageUrl = imageUrl;
    }

    public static OrderedItemResult from(OrderItem orderItem) {
        return new OrderedItemResult(
                orderItem.getId(),
                orderItem.getProductName(),
                orderItem.getProductPrice(),
                orderItem.getProductQuantity(),
                orderItem.getProductImage()
        );
    }

    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
