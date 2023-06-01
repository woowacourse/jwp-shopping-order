package cart.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final LocalDateTime orderAt;
    private final Integer payAmount;
    private final OrderStatus orderStatus;
    private final List<QuantityAndProduct> quantityAndProducts;

    public Order(Long id, Member member, LocalDateTime orderAt, Integer payAmount, OrderStatus orderStatus,
        List<QuantityAndProduct> quantityAndProducts) {
        this.id = id;
        this.member = member;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.orderStatus = orderStatus;
        this.quantityAndProducts = quantityAndProducts;
    }

    public Order(Member member, LocalDateTime orderAt, Integer payAmount, OrderStatus orderStatus,
        List<QuantityAndProduct> quantityAndProducts) {
        this.id = null;
        this.member = member;
        this.orderAt = orderAt;
        this.payAmount = payAmount;
        this.orderStatus = orderStatus;
        this.quantityAndProducts = quantityAndProducts;
    }

    public Order cancel() {
        return new Order(id, member, orderAt, payAmount, OrderStatus.CANCELLED, quantityAndProducts);
    }

    public Long getId() {
        return id;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<QuantityAndProduct> getQuantityAndProducts() {
        return quantityAndProducts;
    }
}
