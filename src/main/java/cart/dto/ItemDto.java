package cart.dto;

import cart.domain.cart.Item;
import cart.domain.cart.Product;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 상품 정보")
public class ItemDto {

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

    public ItemDto(
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

    public static ItemDto from(final Item item) {
        final Product product = item.getProduct();
        return new ItemDto(
                item.getId(),
                product.getName(),
                product.getPrice().getLongValue(),
                product.getImageUrl(),
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
