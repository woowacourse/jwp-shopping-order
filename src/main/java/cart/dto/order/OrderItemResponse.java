package cart.dto.order;

import cart.domain.order.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상품 정보")
public class OrderItemResponse {

    @Schema(description = "상품 Id", example = "1")
    private final Long orderItemId;

    @Schema(description = "상품명", example = "치즈피자")
    private final String name;

    @Schema(description = "이미지", example = "치즈피자.png")
    private final String imageUrl;

    @Schema(description = "가격", example = "8900")
    private final long price;

    @Schema(description = "수량", example = "3")
    private final int quantity;

    public OrderItemResponse(
            final Long orderItemId,
            final String name,
            final long price,
            final String imageUrl,
            final int quantity
    ) {
        this.orderItemId = orderItemId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItemResponse from(final OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getName(),
                item.getPrice().getLongValue(),
                item.getImageUrl(),
                item.getQuantity()
        );
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
