package cart.dto;

import cart.domain.Product;

public class ProductResponse {
    private Long productId;
    private int price;
    private String name;
    private String imageUrl;
    private int stock;

    public ProductResponse(Long productId, int price, String name, String imageUrl, int stock) {
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getPrice(), product.getName(), product.getImageUrl(), product.getStock());
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
