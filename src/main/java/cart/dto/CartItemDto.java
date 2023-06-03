package cart.dto;

import cart.domain.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Schema(description = "장바구니 상품")
public class CartItemDto {

    @Schema(description = "장바구니 상품 id", example = "1")
    private Long id;
    @Schema(description = "장바구니 상품 수량", example = "1")
    private int quantity;
    private ProductResponse product;

    public CartItemDto(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemDto from(final CartItem cartItem) {
        final ProductResponse productDto = ProductResponse.from(cartItem.getProduct());
        return new CartItemDto(cartItem.getId(), cartItem.getQuantity(), productDto);
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
