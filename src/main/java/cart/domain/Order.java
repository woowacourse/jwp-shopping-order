package cart.domain;

public class Order {

    private final Long id;
    private final Member member;
    private final OrderItems orderItems;

    public Order(final Long id, final Member member, final OrderItems orderItems) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }
}
