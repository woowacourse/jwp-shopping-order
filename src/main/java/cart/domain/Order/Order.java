package cart.domain.Order;

import cart.domain.Member.Member;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private final Long id;

    private final Timestamp orderDate;
    private final Member member;

    List<OrderItem> orderItem;

    public Order(Long id, Timestamp orderDate, Member member, List<OrderItem> orderItem) {
        this.id = id;
        this.orderDate = orderDate;
        this.member = member;
        this.orderItem = orderItem;
    }

    public Order(List<OrderItem> orderItem) {
        this(null, null, null, orderItem);
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public int getTotalPrice() {
        return orderItem.stream()
                .mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }

    public Long getId() {
        return id;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalArgumentException("로그인 한 회원의 주문내역이 아닙니다.");
        }
    }
}
