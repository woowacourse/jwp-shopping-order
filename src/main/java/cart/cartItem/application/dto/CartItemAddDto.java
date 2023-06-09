package cart.cartItem.application.dto;

public class CartItemAddDto {
    private Long productId;

    public CartItemAddDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
