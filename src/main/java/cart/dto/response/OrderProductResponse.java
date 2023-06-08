package cart.dto.response;

public class OrderProductResponse {
    private final Long productId;
    private final String name;
    private final String imageUrl;
    private final Integer quantity;
    private final Integer price;
    private final Integer totalPrice;

    public OrderProductResponse(Long productId, String name, String imageUrl, Integer quantity, Integer price, Integer totalPrice) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
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

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
