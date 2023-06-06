package cart.dto;

import cart.domain.order.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "주문 상품")
public class OrderItemDto {

    @Schema(description = "주문 상품 id", example = "1")
    private final Long orderItemId;
    @Schema(description = "이름", example = "치즈피자")
    private final String name;
    @Schema(description = "가격", example = "8900")
    private final BigDecimal price;
    @Schema(description = "이미지", example = "치즈피자.png")
    private final String imageUrl;
    @Schema(description = "수량", example = "4")
    private final long quantity;

    public OrderItemDto(final Long orderItemsId, final String name, final BigDecimal price, final String imageUrl, final long quantity) {
        this.orderItemId = orderItemsId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItemDto from(final OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getName(),
                orderItem.getPrice().getValue(),
                orderItem.getImageUrl(),
                orderItem.getQuantity()
        );
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getQuantity() {
        return quantity;
    }
}
