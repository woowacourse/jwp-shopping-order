package cart.dto;

import cart.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "주문 정보 응답")
public class OrderDetailResponse {

    @Schema(description = "주문 ID", example = "1")
    private Long id;
    @Schema(description = "주문 시 사용한 포인트", example = "400")
    private int usedPoint;
    @Schema(description = "주문 후 적립 된 포인트", example = "100")
    private int savedPoint;
    @Schema(description = "주문 한 시각", example = "2022-06-04T22:30:00")
    private LocalDateTime orderedAt;
    @Schema(description = "주문 상품 정보")
    private List<OrderItemResponse> products;

    public OrderDetailResponse() {
    }

    public OrderDetailResponse(
            final Long id,
            final int usedPoint,
            final int savedPoint,
            final LocalDateTime orderedAt,
            final List<OrderItemResponse> products
    ) {
        this.id = id;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderedAt = orderedAt;
        this.products = products;
    }

    public static OrderDetailResponse from(final Order order) {
        return new OrderDetailResponse(
                order.getId(),
                order.getUsedPoint(),
                order.getSavedPoint(),
                order.getOrderedAt(),
                OrderItemResponse.from(order.getOrderItems())
        );
    }

    public Long getId() {
        return id;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }
}
