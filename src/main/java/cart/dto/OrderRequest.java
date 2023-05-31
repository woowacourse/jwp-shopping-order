package cart.dto;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public class OrderRequest {

    @NonNull
    private final List<Long> id;
    @NonNull
    private final Integer price;
    @Nullable
    private final Long couponId;

    public OrderRequest(final List<Long> id, final Integer price, final Long couponId) {
        this.id = id;
        this.price = price;
        this.couponId = couponId;
    }

    public List<Long> getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getCouponId() {
        return couponId;
    }
}
