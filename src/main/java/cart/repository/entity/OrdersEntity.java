package cart.repository.entity;

import cart.domain.order.Order;

public class OrdersEntity {

    private Long id;
    private Integer originalPrice;
    private Integer actualPrice;
    private Integer deliveryFee;
    private Long memberId;

    public OrdersEntity(Long id, Integer originalPrice, Integer actualPrice, Integer deliveryFee, Long memberId) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.actualPrice = actualPrice;
        this.deliveryFee = deliveryFee;
        this.memberId = memberId;
    }

    public static OrdersEntity of(Order order) {
        return new OrdersEntity(
                order.getId(),
                order.getOriginalTotalItemPrice().getValue(),
                order.getActualTotalItemPrice().getValue(),
                order.getDeliveryFee().getValue(),
                order.getMember().getId()
        );
    }

    public Long getId() {
        return id;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getActualPrice() {
        return actualPrice;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public Long getMemberId() {
        return memberId;
    }
}
