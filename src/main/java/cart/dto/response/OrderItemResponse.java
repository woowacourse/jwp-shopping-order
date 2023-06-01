package cart.dto.response;

public class OrderItemResponse {
    private Long productId;
    private String name;
    private String imageUrl;
    private Long price;
    private Integer quantity;

    private OrderItemResponse() {
    }

    public OrderItemResponse(Long productId, String name, String imageUrl, Long price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
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

    public Long getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
