package cart.dto;

import javax.validation.constraints.NotNull;

public class OrderItemDto {

    @NotNull(message = "장바구니 상품 id가 입력되지 않았습니다.")
    private Long cartItemId;

    public OrderItemDto() {
    }

    public OrderItemDto(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}
