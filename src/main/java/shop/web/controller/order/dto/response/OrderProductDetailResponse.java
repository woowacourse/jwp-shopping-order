package shop.web.controller.order.dto.response;

import shop.application.product.dto.ProductDto;

public class OrderProductDetailResponse {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    private OrderProductDetailResponse() {
    }

    private OrderProductDetailResponse(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static OrderProductDetailResponse of(ProductDto productDto) {
        return new OrderProductDetailResponse(
                productDto.getId(),
                productDto.getName(),
                productDto.getPrice(),
                productDto.getImageUrl()
        );
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
