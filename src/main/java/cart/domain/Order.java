package cart.domain;

import java.util.List;

public class Order {
    private Member member;
    private List<OrderItem> orderItems;

    public Order(Member member, List<OrderItem> orderItems) {
        this.member = member;
        this.orderItems = orderItems;
    }
}
