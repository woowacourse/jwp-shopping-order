package cart.entity;

import cart.domain.Member;
import cart.domain.order.Order;

import java.sql.Timestamp;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Timestamp time;
    private final int deliveryPrice;
    private final int discountFromTotal;

    public OrderEntity(Long id, Long memberId, Timestamp time, int deliveryPrice, int discountFromTotal) {
        this.id = id;
        this.memberId = memberId;
        this.time = time;
        this.deliveryPrice = deliveryPrice;
        this.discountFromTotal = discountFromTotal;
    }

    public static OrderEntity of(Member member, Order order) {
        return new OrderEntity(null,
                member.getId(),
                order.getTime(),
                order.getDeliveryPrice().getValue(),
                order.getDiscountPriceFromTotal().getValue()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Timestamp getTime() {
        return time;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public int getDiscountFromTotal() {
        return discountFromTotal;
    }
}
