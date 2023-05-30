package cart.dto;

import cart.domain.product.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    public ProductResponse() {
    }

    private ProductResponse(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getNameValue(), product.getPriceValue(), product.getImageUrlValue());
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

    public String getImageUrl() {
        return imageUrl;
    }
}
