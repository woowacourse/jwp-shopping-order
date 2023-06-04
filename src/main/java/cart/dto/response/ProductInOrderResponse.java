package cart.dto.response;

public class ProductInOrderResponse {
    private final Long id;
    private final String name;
    private final Integer price;
    private final Integer count;
    private final String imageUrl;

    public ProductInOrderResponse(
            final Long id,
            final String name,
            final Integer price,
            final Integer count,
            final String imageUrl
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getCount() {
        return count;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
