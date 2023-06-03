package shop.application.product.dto;

import shop.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    private ProductDto() {
    }

    private ProductDto(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductDto of(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    public static List<ProductDto> of(List<Product> products) {
        return products.stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
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
