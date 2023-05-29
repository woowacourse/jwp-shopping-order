package cart.dto;

import cart.domain.product.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;

    private ProductResponse(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse from(Product product) {

        return new ProductResponse(product.getId(), product.getNameValue(), product.getPriceValue(), product.getImageUrlValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
