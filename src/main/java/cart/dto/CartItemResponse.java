package cart.dto;

import cart.domain.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니 아이템 응답")
public class CartItemResponse {

    @Schema(description = "장바구니 아이템 ID", example = "1")
    private final Long id;
    @Schema(description = "장바구니 아이템 수량", example = "3")
    private final int quantity;
    @Schema(description = "장바구니 상품 정보")
    private final ProductResponse product;

    private CartItemResponse(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductResponse.from(cartItem.getProduct())
        );
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
