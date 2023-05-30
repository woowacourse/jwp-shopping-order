package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long orderId;
    private final LocalDateTime orderAt;
    private final Integer payAmount;
    private final Integer usedPoint;
    private final Integer savedPoint;
    private final OrderStatus orderStatus;
    private final List<QuantityAndProduct> products;

    public Order(Long orderId, LocalDateTime orderAt, Integer payAmount, Integer usedPoint, Integer savedPoint,
        OrderStatus orderStatus, List<QuantityAndProduct> products) {
        this.orderId = orderId;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderStatus = orderStatus;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<QuantityAndProduct> getProducts() {
        return products;
    }
}
