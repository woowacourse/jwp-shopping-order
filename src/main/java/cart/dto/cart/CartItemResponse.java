package cart.dto.cart;

import cart.domain.cart.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니 상품")
public class CartItemResponse {

    @Schema(description = "장바구니 상품 id", example = "1")
    private Long id;

    @Schema(description = "장바구니 상품 수량", example = "1")
    private Integer quantity;
    private ProductResponse product;

    public CartItemResponse(final Long id, final Integer quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse from(final CartItem cartItem) {
        final ProductResponse productResponse = ProductResponse.from(cartItem.getProduct());
        return new CartItemResponse(cartItem.getId(), cartItem.getQuantity(), productResponse);
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
