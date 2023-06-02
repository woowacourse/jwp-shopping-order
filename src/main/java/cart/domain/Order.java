package cart.domain;

import java.util.List;

public class Order {

    private Long id;
    private Long memberId;
    private List<OrderItem> orderItems;
    private Integer totalPrice;

    public Order(Long memberId, List<OrderItem> orderItems, Integer totalPrice) {
        this(null, memberId, orderItems, totalPrice);
    }

    public Order(Long id, Long memberId, List<OrderItem> orderItems, Integer totalPrice) {
        this.id = id;
        this.memberId = memberId;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
