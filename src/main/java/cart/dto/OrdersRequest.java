package cart.dto;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrdersRequest {
    @NotNull
    private List<Long> selectCartIds;
    @Nullable
    private Long couponId;

    private OrdersRequest() {

    }

    public OrdersRequest(List<Long> selectCartIds, Long couponId) {
        this.selectCartIds = selectCartIds;
        this.couponId = couponId;
    }

    public List<Long> getSelectCartIds() {
        return selectCartIds;
    }

    public Optional<Long> getCouponId() {
        return Optional.ofNullable(couponId);
    }
}
