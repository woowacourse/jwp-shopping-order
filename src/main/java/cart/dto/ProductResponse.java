package cart.dto;

import cart.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private int stock;

    private ProductResponse(Long id, String name, int price, String imageUrl, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getStock());
    }

    public static List<ProductResponse> createProductResponses(List<Product> products) {
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
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

    public int getStock() {
        return stock;
    }
}
