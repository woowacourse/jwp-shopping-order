package cart.dto.product;

import cart.domain.Product;
import java.util.List;
import java.util.stream.Collectors;

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
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static List<ProductResponse> from(List<Product> products) {
        return products.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice(),
                        product.getImageUrl()))
                .collect(Collectors.toUnmodifiableList());
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
