package cart.domain.order;

import cart.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Member member;
    private List<OrderProduct> orderProducts;
    private int usedPoint;
    private LocalDateTime orderedAt;

    private Order() {
    }

    public Order(Long id, Member member, List<OrderProduct> orderProducts, int usedPoint, LocalDateTime orderedAt) {
        this.id = id;
        this.member = member;
        this.orderProducts = orderProducts;
        this.usedPoint = usedPoint;
        this.orderedAt = orderedAt;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
