package cart.presentation.dto.response;

public class OrderInfo {

    private final Long productId;
    private final Integer price;
    private final String name;
    private final String imageUrl;
    private final Integer quantity;

    public OrderInfo(Long productId, Integer price, String name, String imageUrl, Integer quantity) {
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getPrice() {
        return price;
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
}
