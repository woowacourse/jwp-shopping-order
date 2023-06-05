package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상품")
public class OrderItemDto {

    @Schema(description = "상품 Id", example = "1")
    private final Long orderItemId;

    @Schema(description = "상품명", example = "커피")
    private final String name;

    @Schema(description = "가격", example = "1000")
    private final Long price;

    @Schema(description = "이미지 URL", example = "커피.jpg")
    private final String imageUrl;

    @Schema(description = "수량", example = "10")
    private final Integer quantity;

    public OrderItemDto(final Long orderItemId, final String name, final Long price, final String imageUrl,
                        final Integer quantity) {
        this.orderItemId = orderItemId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
