package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long orderId;
    private final LocalDateTime orderAt;
    private final Integer payAmount;
    private final Integer usedPoint;
    private final Integer savedPoint;
    //TODO: 주문 상태 관련
    private final OrderStatus orderState;
    private final List<QuantityAndProduct> products;

    public Order(Long orderId, LocalDateTime orderAt, Integer payAmount, Integer usedPoint, Integer savedPoint,
        OrderStatus orderState, List<QuantityAndProduct> products) {
        this.orderId = orderId;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderState = orderState;
        this.products = products;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public Integer getPayAmount() {
        return payAmount;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public Integer getSavedPoint() {
        return savedPoint;
    }

    public OrderStatus getOrderState() {
        return orderState;
    }

    public List<QuantityAndProduct> getProducts() {
        return products;
    }
}
