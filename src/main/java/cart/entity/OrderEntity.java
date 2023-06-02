package cart.entity;

import cart.domain.Order;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final int discountedAmount;
    private final int deliveryAmount;
    private final String address;

    public OrderEntity(final Long id, final Long memberId, final int discountedAmount, final int deliveryAmount,
                       final String address) {
        this.id = id;
        this.memberId = memberId;
        this.discountedAmount = discountedAmount;
        this.deliveryAmount = deliveryAmount;
        this.address = address;
    }

    public static OrderEntity of(final Order order, final Long memberId) {
        return new OrderEntity(order.getId(), memberId, order.discountProductAmount().getValue(),
                order.getDeliveryAmount().getValue(), order.getAddress());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getDiscountedAmount() {
        return discountedAmount;
    }

    public int getDeliveryAmount() {
        return deliveryAmount;
    }

    public String getAddress() {
        return address;
    }
}
