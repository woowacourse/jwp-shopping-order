package shop.web.controller.product.dto.response;

import shop.application.product.dto.ProductDto;

import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    private ProductResponse() {
    }

    private ProductResponse(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse of(ProductDto productDto) {
        return new ProductResponse(
                productDto.getId(),
                productDto.getName(),
                productDto.getPrice(),
                productDto.getImageUrl()
        );
    }

    public static List<ProductResponse> of(List<ProductDto> productDtos) {
        return productDtos.stream()
                .map(ProductResponse::of)
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
