package shop.application.cart.dto;

import shop.application.product.dto.ProductDto;
import shop.domain.cart.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartDto {
    private Long id;
    private int quantity;
    private ProductDto product;

    private CartDto() {
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

    public static List<CartDto> of(List<CartItem> cartItem) {
        return cartItem.stream()
                .map(CartDto::of)
                .collect(Collectors.toList());
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
