package cart.entity;

import cart.domain.Order;

public class OrderEntity {

    private Long id;
    private Long memberId;

    public OrderEntity(Long memberId) {
        this(null, memberId);
    }

    public OrderEntity(Long id, Long memberId) {
        this.id = id;
        this.memberId = memberId;
    }

    public static OrderEntity from(Order order) {
        return new OrderEntity(order.getMemberId());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }
}
