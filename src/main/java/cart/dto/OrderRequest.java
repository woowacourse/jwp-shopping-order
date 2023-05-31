package cart.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderInfo> orderInfos;
    private Long payment;
    private Long point;

    public OrderRequest() {
    }

    public OrderRequest(final List<OrderInfo> orderInfos, final Long payment, final Long point) {
        this.orderInfos = orderInfos;
        this.payment = payment;
        this.point = point;
    }

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public Long getPayment() {
        return payment;
    }

    public Long getPoint() {
        return point;
    }
}
