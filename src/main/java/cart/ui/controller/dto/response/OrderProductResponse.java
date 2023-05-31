package cart.ui.controller.dto.response;

import cart.domain.order.OrderProduct;
import cart.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상품 응답")
public class OrderProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품 이름", example = "치킨")
    private String name;

    @Schema(description = "상품 가격", example = "10000")
    private int price;

    @Schema(description = "상품 이미지 경로", example = "http://chicken.com")
    private String imageUrl;

    @Schema(description = "상품 수량", example = "5")
    private int quantity;

    private OrderProductResponse() {
    }

    private OrderProductResponse(Long productId, String name, int price, String imageUrl, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderProductResponse from(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        return new OrderProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                orderProduct.getQuantity()
        );
    }

    public Long getProductId() {
        return productId;
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

    public int getQuantity() {
        return quantity;
    }
}
