package cart.entity;

import cart.domain.Order;

public class OrderEntity {

    private Long id;
    private Long memberId;
    private Integer totalPrice;
    private Integer deliveryFee;

    public OrderEntity(Long memberId, Integer totalPrice, Integer deliveryFee) {
        this(null, memberId, totalPrice, deliveryFee);
    }

    public OrderEntity(Long id, Long memberId, Integer totalPrice, Integer deliveryFee) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.deliveryFee = deliveryFee;
    }

    public static OrderEntity from(Order order) {
        return new OrderEntity(order.getMemberId(), order.getTotalPrice(), order.getDeliveryFee());
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

    public Integer getDeliveryFee() {
        return deliveryFee;
    }
}
