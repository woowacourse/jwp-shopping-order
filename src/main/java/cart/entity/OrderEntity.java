package cart.entity;

import cart.domain.Order;

public class OrderEntity {

    private Long id;
    private Long memberId;
    private Integer totalPrice;

    public OrderEntity(Long memberId, Integer totalPrice) {
        this(null, memberId, totalPrice);
    }

    public OrderEntity(Long id, Long memberId, Integer totalPrice) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
    }

    public static OrderEntity from(Order order) {
        return new OrderEntity(order.getMemberId(), order.getTotalPrice());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
