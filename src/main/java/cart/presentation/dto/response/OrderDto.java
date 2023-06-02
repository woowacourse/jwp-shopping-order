package cart.presentation.dto.response;

public class OrderDto {

    private final Long productId;
    private final Long price;
    private final String name;
    private final String imageUrl;
    private final Long quantity;

    public OrderDto(Long productId, Long price, String name, String imageUrl, Long quantity) {
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
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
