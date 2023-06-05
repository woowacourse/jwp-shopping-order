package cart.dto.response;

import cart.domain.product.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;

    private ProductResponse(Long id, String name, Long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName().name(),
                product.getPrice().price(),
                product.getImage().imageUrl()
        );
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
}
