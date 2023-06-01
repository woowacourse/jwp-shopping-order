package cart.dto;

import cart.domain.OrderInfo;

public class OrderInfoResponse {
    private final Long productId;
    private final Long price;
    private final String name;
    private final String imageUrl;
    private final Long quantity;
    
    public OrderInfoResponse(final Long productId, final Long price, final String name, final String imageUrl, final Long quantity) {
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }
    
    public static OrderInfoResponse from(final OrderInfo orderInfo) {
        return new OrderInfoResponse(
                orderInfo.getProduct().getId(),
                orderInfo.getProductPrice(),
                orderInfo.getProductName(),
                orderInfo.getProductImageUrl(),
                orderInfo.getQuantity()
        );
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public Long getPrice() {
        return price;
    }
    
    public String getName() {
        return name;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public Long getQuantity() {
        return quantity;
    }
}
