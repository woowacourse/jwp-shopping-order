package cart.ui.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "장바구니 상품 목록 삭제 요청")
public class CartItemRemoveRequest {

    @Schema(description = "장바구니 상품 ID 목록")
    @NotNull(message = "장바구니 상품 ID 목록은 필수로 존재해야 합니다.")
    @NotEmpty(message = "장바구니 상품 ID 목록은 비어있어서는 안됩니다.")
    private List<Long> cartItemIds;

    private CartItemRemoveRequest() {
    }

    public CartItemRemoveRequest(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}
