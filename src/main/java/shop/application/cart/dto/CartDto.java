package shop.application.cart.dto;

import shop.domain.cart.CartItem;
import shop.application.product.dto.ProductDto;

public class CartDto {
    private Long id;
    private int quantity;
    private ProductDto product;

    private CartDto(){
    }

    private CartDto(Long id, int quantity, ProductDto product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartDto of(CartItem cartItem) {
        return new CartDto(
                cartItem.getId(),
                cartItem.getQuantity(),
                ProductDto.of(cartItem.getProduct())
        );
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductDto getProduct() {
        return product;
    }
}
