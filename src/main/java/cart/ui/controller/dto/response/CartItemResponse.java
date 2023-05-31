package cart.ui.controller.dto.response;

import cart.domain.cartitem.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니 상품 응답")
public class CartItemResponse {

    @Schema(description = "장바구니 상품 ID", example = "1")
    private Long id;

    @Schema(description = "장바구니 상품 수량", example = "5")
    private int quantity;

    @Schema(description = "장바구니 상품 정보")
    private ProductResponse product;

    private CartItemResponse() {
    }

    private CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse from(CartItem cartItem) {
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
