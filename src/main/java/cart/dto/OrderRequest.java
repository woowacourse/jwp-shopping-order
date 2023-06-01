package cart.dto;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public class OrderRequest {

    @NonNull
    private final List<Long> cartItemIds;
    @NonNull
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

    public Long getCouponId() {
        return couponId;
    }
}
