package cart.domain;

import java.util.List;

public class Order {

    private Long id;
    private Long memberId;
    private List<OrderItem> orderItems;

    public Order(Long memberId, List<OrderItem> orderItems) {
        this(null, memberId, orderItems);
    }

    public Order(Long id, Long memberId, List<OrderItem> orderItems) {
        this.id = id;
        this.memberId = memberId;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
