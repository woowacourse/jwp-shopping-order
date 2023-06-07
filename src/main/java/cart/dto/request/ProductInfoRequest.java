package cart.dto.request;

public class ProductInfoRequest {

    private final Long productId;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final Integer stock;

    private ProductInfoRequest() {
        this(null, null, null, null, null);
    }

    public ProductInfoRequest(final Long productId, final String name, final Integer price, final String imageUrl, final Integer stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
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

    public Integer getStock() {
        return stock;
    }
}
