package cart.dto;

import java.util.List;

public class OrderDetailHistoryResponse {
    private final Long id;
    private final String time;
    private final List<OrderItemHistoryResponse> orderItemHistoryResponses;
    private final int deliveryPrice;
    private final int discountFromTotalPrice;

    public OrderDetailHistoryResponse(Long id, String time, List<OrderItemHistoryResponse> orderItemHistoryResponses, int deliveryPrice, int discountFromTotalPrice) {
        this.id = id;
        this.time = time;
        this.orderItemHistoryResponses = orderItemHistoryResponses;
        this.deliveryPrice = deliveryPrice;
        this.discountFromTotalPrice = discountFromTotalPrice;
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public List<OrderItemHistoryResponse> getOrderItemHistoryResponses() {
        return orderItemHistoryResponses;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public int getDiscountFromTotalPrice() {
        return discountFromTotalPrice;
    }
}
