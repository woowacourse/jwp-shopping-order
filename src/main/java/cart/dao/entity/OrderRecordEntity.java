package cart.dao.entity;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;

import java.sql.Timestamp;
import java.util.List;

public class OrderRecordEntity {
    private final Long id;
    private final long memberId;
    private final Timestamp orderTime;

    public OrderRecordEntity(final Long id, final long memberId, final Timestamp orderTime) {
        this.id = id;
        this.memberId = memberId;
        this.orderTime = orderTime;
    }

    public static OrderRecordEntity from(final Order order) {
        return new OrderRecordEntity(order.getId(), order.getMember().getId(), Timestamp.valueOf(order.getOrderTime()));
    }

    public long getMemberId() {
        return this.memberId;
    }

    public Timestamp getOrderTime() {
        return this.orderTime;
    }

    public long getId() {
        return this.id;
    }

    public Order toOrder(final Member member, final List<OrderItem> orderItems) {
        return new Order(this.id, member, orderItems, this.orderTime.toLocalDateTime());
    }
}
