package cart.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderItemResponse {

    @Schema(description = "주문 상품 아이디", example = "1")
    private final Long id;
    @Schema(description = "주문 상품 수량", example = "3")
    private final int quantity;
    @Schema(description = "주문 상품")
    private final ProductResponse product;

    public OrderItemResponse(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
