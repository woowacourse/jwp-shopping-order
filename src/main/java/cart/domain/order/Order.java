package cart.domain.order;

import cart.domain.Member;
import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;

    private Order(final Long id, final Member member, final OrderItems orderItems) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
    }

    public static Order beforePersisted(final Member member, final OrderItems orderItems) {
        return new Order(null, member, orderItems);
    }

    public static Order persisted(final Long id, final Member member, final OrderItems orderItems) {
        return new Order(id, member, orderItems);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems.getItems();
    }

    public Long getTotalPrice() {
        return orderItems.getTotalPrice();
    }
}
