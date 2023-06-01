package cart.ui.controller.dto.response;

import cart.domain.order.OrderProduct;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상품 응답")
public class OrderProductResponse {

    @Schema(description = "상품 수량", example = "5")
    private int quantity;

    @Schema(description = "상품 정보")
    private ProductResponse product;

    private OrderProductResponse() {
    }

    public OrderProductResponse(int quantity, ProductResponse product) {
        this.quantity = quantity;
        this.product = product;
    }

    public static OrderProductResponse from(OrderProduct orderProduct) {
        return new OrderProductResponse(
                orderProduct.getQuantity(),
                ProductResponse.from(orderProduct.getProduct())
        );
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
