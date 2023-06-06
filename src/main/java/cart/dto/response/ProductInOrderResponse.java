package cart.dto.response;

public class ProductInOrderResponse {
    private final Long id;
    private final String name;
    private final Integer singleProductPrice;
    private final Integer quantity;
    private final String imageUrl;

    public ProductInOrderResponse(
            final Long id,
            final String name,
            final Integer singleProductPrice,
            final Integer quantity,
            final String imageUrl
    ) {
        this.id = id;
        this.name = name;
        this.singleProductPrice = singleProductPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSingleProductPrice() {
        return singleProductPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
