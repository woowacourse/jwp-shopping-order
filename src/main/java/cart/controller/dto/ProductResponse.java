package cart.controller.dto;

import cart.domain.Product;
import cart.domain.order.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

public class ProductResponse {

    @Schema(description = "아이디", example = "1")
    private Long id;
    @Schema(description = "이름", example = "치킨")
    private String name;
    @Schema(description = "가격", example = "10000")
    private int price;
    @Schema(description = "이미지 URL", example = "imgUrl")
    private String imageUrl;

    private ProductResponse(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static ProductResponse from(OrderItem product) {
        return new ProductResponse(product.getId(),
                product.getProduct().getName(),
                product.getProduct().getPrice(),
                product.getProduct().getImageUrl()
        );
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
