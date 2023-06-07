package cart.dto.response;

import cart.domain.OrderItemEntity;
import cart.domain.Product;

public class ProductResponse {

    private final Long productId;
    private final int price;
    private final String name;
    private final String imageUrl;
    private final int stock;

    public ProductResponse(Long productId, int price, String name, String imageUrl, int stock) {
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getPrice(), product.getName(), product.getImageUrl(), product.getStock());
    }

    public static ProductResponse of(final OrderItemEntity orderItemEntity, final Product product) {
        return new ProductResponse(
                orderItemEntity.getProductId(),
                orderItemEntity.getProductPrice(),
                orderItemEntity.getProductName(),
                orderItemEntity.getProductImageUrl(),
                product.getStock()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }
}
