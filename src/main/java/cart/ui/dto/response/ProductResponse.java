package cart.ui.dto.response;

import cart.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public ProductResponse() {
    }

    private ProductResponse(final Long id, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getAmount().getValue(),
            product.getImageUrl());
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
