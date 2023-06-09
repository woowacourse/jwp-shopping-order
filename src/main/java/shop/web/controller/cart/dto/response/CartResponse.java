package shop.web.controller.cart.dto.response;

import shop.application.cart.dto.CartDto;
import shop.web.controller.product.dto.response.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;

    private CartResponse() {
    }

    private CartResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartResponse of(CartDto cartDto) {
        return new CartResponse(
                cartDto.getId(),
                cartDto.getQuantity(),
                ProductResponse.of(cartDto.getProduct())
        );
    }

    public static List<CartResponse> of(List<CartDto> cartDtos) {
        return cartDtos.stream()
                .map(CartResponse::of)
                .collect(Collectors.toList());
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
