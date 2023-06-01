package cart.application.domain;

import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final List<OrderInfo> orderInfo;
    private final int originalPrice;
    private final int usedPoint;
    private final int pointToAdd;

    public Order(Long id, Member member, List<OrderInfo> orderInfo, int originalPrice, int usedPoint, int pointToAdd) {
        this.id = id;
        this.member = member;
        this.orderInfo = orderInfo;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderInfo> getOrderInfo() {
        return orderInfo;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getPointToAdd() {
        return pointToAdd;
    }
}
