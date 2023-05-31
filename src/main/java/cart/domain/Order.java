package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long orderId;
    private final Member member;
    private final LocalDateTime orderAt;
    private final Integer payAmount;
    private final Integer usedPoint;
    private final Integer savedPoint;
    private final OrderStatus orderStatus;
    private final List<QuantityAndProduct> quantityAndProducts;

    public Order(Long orderId, Member member, LocalDateTime orderAt, Integer payAmount, Integer usedPoint, Integer savedPoint,
        OrderStatus orderStatus, List<QuantityAndProduct> quantityAndProducts) {
        this.orderId = orderId;
        this.member = member;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderStatus = orderStatus;
        this.quantityAndProducts = quantityAndProducts;
    }

    public Order(Member member, LocalDateTime orderAt, Integer payAmount, Integer usedPoint, Integer savedPoint,
        OrderStatus orderStatus, List<QuantityAndProduct> quantityAndProducts) {
        this.orderId = null;
        this.member = member;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderStatus = orderStatus;
        this.quantityAndProducts = quantityAndProducts;
    }

    public static Order from(Member member, Integer payAmount, Integer usedPoint, Integer savedPoint,
        List<QuantityAndProduct> quantityAndProducts) {
        return new Order(null, member, LocalDateTime.now(), payAmount, usedPoint, savedPoint, OrderStatus.PENDING,
            quantityAndProducts);
    }

    public Order cancel() {
        return new Order(orderId, member, orderAt, payAmount, usedPoint, savedPoint, OrderStatus.CANCELLED,
            quantityAndProducts);
    }

    public Long getOrderId() {
        return orderId;
    }

    public Member getMember() {
        return member;
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

    public List<QuantityAndProduct> getQuantityAndProducts() {
        return quantityAndProducts;
    }
}
