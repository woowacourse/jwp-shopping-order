package cart.dto;

import java.util.List;
import java.util.Objects;

public class OrdersRequest {
    private List<Long> selectCartIds;
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

    public Long getCouponId() {
        return couponId;
    }
    public boolean isNoCoupon(){
        return Objects.isNull(couponId);
    }
}
