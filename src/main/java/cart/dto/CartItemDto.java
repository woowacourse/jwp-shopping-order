package cart.dto;

import cart.domain.product.Item;

public class CartItemDto {

    private Long id;
    private Integer quantity;
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
