package cart.domain;

import java.time.LocalDateTime;

public class Order {

    private final Long id;
    private final long memberId;
    private final OrderItems orderItems;
    private final LocalDateTime createdAt;

    public Order(final Long id, final long memberId, final OrderItems orderItems, final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
    }

    public Order(final long memberId, final OrderItems orderItems) {
        this(null, memberId, orderItems, null);
    }

    public boolean isPaymentAmountEqual(final int amount) {
        return orderItems.isPaymentAmountEqual(new Price(amount));
    }

    public int getPaymentAmount() {
        return orderItems.calculatePaymentAmount().getAmount();
    }

    public int getOriginalPrice() {
        return orderItems.calculateOriginalPrice().getAmount();
    }

    public int getDiscountPrice() {
        return orderItems.calculateDiscountPrice().getAmount();
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
