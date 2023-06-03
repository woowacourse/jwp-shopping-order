package cart.ui.order.dto;

import java.util.List;

public class CreateOrderDiscountRequest {

    private List<Long> couponIds;
    private Integer point;

    public CreateOrderDiscountRequest() {
    }

    public CreateOrderDiscountRequest(List<Long> couponIds, Integer point) {
        this.couponIds = couponIds;
        this.point = point;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public Integer getPoint() {
        return point;
    }

}
