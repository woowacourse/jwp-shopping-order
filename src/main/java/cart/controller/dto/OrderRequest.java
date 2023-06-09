package cart.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class OrderRequest {

    @NotEmpty(message = "상품 아이디를 입력해주세요.")
    @Schema(description = "장바구니 상품 아이디", allowableValues = {"1", "2", "3"})
    private final List<@NotNull Long> cartItemIds;
    @NotNull(message = "가격은 필수값 입니다.")
    @Positive(message = "가격은 양수 값이어야 합니다.")
    @Schema(description = "상품 총 가격", example = "35000")
    private final Integer price;
    @Nullable
    @Schema(description = "사용한 쿠폰 아이디", example = "1")
    private final Long couponId;

    public OrderRequest(final List<Long> cartItemIds, final Integer price, @Nullable final Long couponId) {
        this.cartItemIds = cartItemIds;
        this.price = price;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Integer getPrice() {
        return price;
    }

    @Nullable
    public Long getCouponId() {
        return couponId;
    }
}
