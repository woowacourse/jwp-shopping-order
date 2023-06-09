package cart.dto.product;

import cart.domain.product.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private Long stock;

    private ProductResponse(Long id, String name, Long price, String imageUrl, Long stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getStock());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getStock() {
        return stock;
    }
}
