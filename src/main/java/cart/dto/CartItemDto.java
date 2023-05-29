package cart.dto;

import cart.domain.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니 상품")
public class CartItemDto {

    @Schema(description = "장바구니 상품 id", example = "1")
    private Long id;
    @Schema(description = "장바구니 상품 수량", example = "1")
    private Integer quantity;
    private ProductDto product;

    public CartItemDto(final Long id, final Integer quantity, final ProductDto product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemDto from(final CartItem cartItem) {
        final ProductDto productDto = ProductDto.from(cartItem.getProduct());
        return new CartItemDto(cartItem.getId(), cartItem.getQuantity(), productDto);
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductDto getProduct() {
        return product;
    }
}
