package cart.application.domain;

import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final List<OrderInfo> orderInfo;
    private final long originalPrice;
    private final long usedPoint;
    private final long pointToAdd;

    public Order(Long id, Member member, List<OrderInfo> orderInfo,
                 long originalPrice, long usedPoint, long pointToAdd) {
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
