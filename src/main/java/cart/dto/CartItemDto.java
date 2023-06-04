package cart.dto;

import cart.domain.product.Item;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItemDto {

    @NotNull(message = "id 필드가 필요합니다.")
    private Long id;

    @NotNull(message = "quantity 필드가 필요합니다.")
    @Positive(message = "상품의 양은 양수여야 합니다.")
    private Integer quantity;

    @NotNull(message = "product 필드가 필요합니다.")
    private ProductDto product;

    public CartItemDto(Long id, Integer quantity, ProductDto product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemDto of(Item item) {
        return new CartItemDto(
                item.getId(),
                item.getQuantity(),
                ProductDto.of(item.getProduct())
        );
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
