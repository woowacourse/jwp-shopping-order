package cart.service.dto;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class
OrderRequest {

    @NotEmpty(message = "상품 아이디를 입력해주세요.")
    private final List<@NotNull Long> cartItemIds;
    @NotNull(message = "가격은 필수값 입니다.")
    @Positive(message = "가격은 양수 값이어야 합니다.")
    private final Integer price;
    @Nullable
    private final Long couponId;

    public OrderRequest(final List<Long> cartItemIds, final Integer price, final Long couponId) {
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
