package cart.dto;

import java.util.List;

public class OrderRequest {

    private List<OrderInfo> orderInfos;
    private Integer payment;
    private Integer point;

    public OrderRequest() {
    }

    public OrderRequest(final List<OrderInfo> orderInfos, final Integer payment, final Integer point) {
        this.orderInfos = orderInfos;
        this.payment = payment;
        this.point = point;
    }

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public Integer getPayment() {
        return payment;
    }

    public Integer getPoint() {
        return point;
    }
}
