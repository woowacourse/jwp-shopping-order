package cart.domain.order;

import cart.domain.member.Member;
import cart.domain.member.Point;

public class Order {

    private final Long id;
    private final Member member;
    private final Point usingPoint;
    private final OrderProducts orderProducts;

    public Order(final Long id, final Member member, final int usingPoint, final OrderProducts orderProducts) {
        this.id = id;
        this.member = member;
        this.usingPoint = new Point(usingPoint);
        this.orderProducts = orderProducts;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public int getUsedPoint() {
        return usingPoint.getValue();
    }

    public OrderProducts getOrderProducts() {
        return orderProducts;
    }

    public Integer getTotalAmount() {
        return orderProducts.getTotalAmount() - usingPoint.getValue();
    }

    public Integer getSavedPoint() {
        return orderProducts.getSavedPoint();
    }
}
