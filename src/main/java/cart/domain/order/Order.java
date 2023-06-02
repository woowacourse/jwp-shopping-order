package cart.domain.order;

import cart.exception.OrderUnauthorizedException;

import java.util.List;

public class Order {

    private Long id;
    private Long memberId;
    private int deliveryFee;
    private List<OrderItem> orderItems;
    private int total;

    public Order(final Long id, final Long memberId, final int deliveryFee, final List<OrderItem> orderItems) {
        this.id = id;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
        this.total = calculateTotal(orderItems);
    }

    private int calculateTotal(final List<OrderItem> orderItems) {
        return orderItems.stream().mapToInt(OrderItem::getTotal).sum() + deliveryFee;
    }

    public Order(final Long id, final Long memberId, final int deliveryFee) {
        this.id = id;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getTotal() {
        return total;
    }

    public void checkOwner(Long memberId) {
        if (this.memberId != memberId) {
            throw new OrderUnauthorizedException();
        }
    }
}
