package cart.domain;

public class Order {

    private final Member member;
    private final OrderItems orderItems;
    private final Payment payment;

    public Order(Member member, OrderItems orderItems, Payment payment) {
        this.member = member;
        this.orderItems = orderItems;
        this.payment = payment;
    }

    public Member getMember() {
        return member;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public Payment getPayment() {
        return payment;
    }
}
