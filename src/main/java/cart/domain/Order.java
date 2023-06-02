package cart.domain;

import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.exception.OrderException;
import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final Member member;
    private final Money deliveryFee;
    private final OrderStatus status;
    private final List<OrderItem> orderItems;

    public Order(final Long id,
                 final Member member,
                 final Money deliveryFee,
                 final OrderStatus status,
                 final List<OrderItem> orderItems) {
        this.id = id;
        this.member = member;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.orderItems = orderItems;
    }

    public Order(final Member member,
                 final Money deliveryFee,
                 final List<OrderItem> orderItems) {
        this(null, member, deliveryFee, cart.domain.OrderStatus.COMPLETE, orderItems);
    }

    public static Order from(final OrderEntity order, final List<OrderItemEntity> orderItems) {
        return new Order(order.getId(),
                new Member(order.getMemberId()),
                new Money(order.getDeliveryFee()),
                OrderStatus.find(order.getStatus()),
                OrderItem.from(orderItems));
    }

    public void checkTotalPrice(final Long totalPrice) {
        if (!Objects.equals(totalProductPrice(), totalPrice)) {
            throw new OrderException.OutOfDatedProductPrice();
        }
    }

    public Long totalProductPrice() {
        return orderItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Order cancel() {
        if (isCanceled()) {
            throw new OrderException.AlreadyCanceledOrder(id);
        }
        return new Order(id, member, deliveryFee, OrderStatus.CANCEL, orderItems);
    }

    public boolean isCanceled() {
        return status == OrderStatus.CANCEL;
    }
}
