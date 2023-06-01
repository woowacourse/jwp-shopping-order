package cart.dto;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class OrderCartItemDto {

    @NotNull(message = "장바구니 ID를 입력해야 합니다")
    private final Long cartItemId;

    @NotBlank(message = "주문한 상품의 이름을 입력해야 합니다")
    private final String orderCartItemName;

    @NotNull(message = "주문한 상품의 가격을 입력해야 합니다")
    @PositiveOrZero(message = "주문한 상품의 가격은 0원 이상이어야 합니다")
    private final int orderCartItemPrice;

    @NotBlank(message = "주문한 상품 이미지 경로를 입력해야 합니다")
    @URL(message = "이미지 입력 형식에 맞지 않습니다")
    private final String orderCartItemImageUrl;

    public OrderCartItemDto(final Long cartItemId, final String orderCartItemName, final int orderCartItemPrice, final String orderCartItemImageUrl) {
        this.cartItemId = cartItemId;
        this.orderCartItemName = orderCartItemName;
        this.orderCartItemPrice = orderCartItemPrice;
        this.orderCartItemImageUrl = orderCartItemImageUrl;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public String getOrderCartItemName() {
        return orderCartItemName;
    }

    public int getOrderCartItemPrice() {
        return orderCartItemPrice;
    }

    public String getOrderCartItemImageUrl() {
        return orderCartItemImageUrl;
    }
}
