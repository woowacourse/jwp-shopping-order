package cart.domain.order;

import static cart.exception.OrderException.IllegalMember;

import cart.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private Member member;
    private List<OrderProduct> orderProducts;
    private int usedPoint;
    private int deliveryFee;
    private LocalDateTime orderedAt;

    private Order() {
    }

    public Order(
            Long id,
            Member member,
            List<OrderProduct> orderProducts,
            int usedPoint,
            int deliveryFee,
            LocalDateTime orderedAt
    ) {
        this.id = id;
        this.member = member;
        this.orderProducts = orderProducts;
        this.usedPoint = usedPoint;
        this.deliveryFee = deliveryFee;
        this.orderedAt = orderedAt;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMember();
        }
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

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
