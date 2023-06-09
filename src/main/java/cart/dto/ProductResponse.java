package cart.dto;

import cart.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final long price;
    private final String imageUrl;

    private ProductResponse(final Long id, final String name, final long price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().getValue(),
                product.getImageUrl());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
