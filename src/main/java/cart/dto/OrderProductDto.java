package cart.dto;

public class OrderProductDto {

    private final Long productId;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer quantity;

    public OrderProductDto(final Long productId, final String name, final Integer price, final String imageUrl, final Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
