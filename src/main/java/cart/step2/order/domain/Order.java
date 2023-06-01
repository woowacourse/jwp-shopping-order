package cart.step2.order.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private final Long id;
    private final int price;
    private final Long couponId;
    private final Long memberId;
    private final LocalDateTime date;
    private final List<OrderItem> orderItems;

    private Order(final Long id, final int price, final Long couponId, final Long memberId, final LocalDateTime date, final List<OrderItem> orderItems) {
        this.id = id;
        this.price = price;
        this.couponId = couponId;
        this.memberId = memberId;
        this.date = date;
        this.orderItems = orderItems;
    }

    public static Order createNonePkAndOrderItemsOrder(final int price, final Long couponId, final Long memberId) {
        return new Order(null, price, couponId, memberId, null, null);
    }

    public static Order of(final Long id, final int price, final Long couponId, final Long memberId, final LocalDateTime date, final List<OrderItem> orderItems) {
        return new Order(id, price, couponId, memberId, date, orderItems);
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

}
