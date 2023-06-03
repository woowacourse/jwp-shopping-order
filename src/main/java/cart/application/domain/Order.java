package cart.application.domain;

import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final OrderInfos orderInfos;
    private final long originalPrice;
    private final long usedPoint;
    private final long pointToAdd;

    public Order(Long id, Member member, OrderInfos orderInfo,
                 long originalPrice, long usedPoint, long pointToAdd) {
        this.id = id;
        this.member = member;
        this.orderInfos = orderInfo;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public void adjustPoint() {
        usePoint();
        earnPoint();
    }

    public void usePoint() {
        long availablePoint = orderInfos.calculateAvailablePoint();
        if (availablePoint < usedPoint) {
            throw new IllegalArgumentException(); // TODO: 예외 변경
        }
        member.usePoint(usedPoint);
    }

    public void earnPoint() {
        long earnedPoint = orderInfos.calculateEarnedPoint();
        member.addPoint(earnedPoint);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public OrderInfos getOrderInfo() {
        return orderInfos;
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public long getUsedPoint() {
        return usedPoint;
    }

    public long getPointToAdd() {
        return pointToAdd;
    }
}
