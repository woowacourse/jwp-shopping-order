package cart.dto;

import cart.domain.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "주문 상품 응답")
public class OrderItemResponse {

    @Schema(description = "주문한 상품 id", example = "1")
    private final Long id;
    @Schema(description = "주문한 상품 수량", example = "3")
    private final int quantity;
    @Schema(description = "주문한 상품 상세 정보")
    private final ProductResponse product;

    public OrderItemResponse(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static List<OrderItemResponse> from(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemResponse(
                        orderItem.getId(),
                        orderItem.getQuantity(),
                        ProductResponse.from(orderItem.getProduct())
                ))
                .collect(Collectors.toUnmodifiableList());
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
