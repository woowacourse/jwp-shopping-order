package cart.domain;

import java.util.List;

public class Order {
    private final Long id;
    private final Member member;
    private final OrderInfos orderInfos;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;
    
    public Order(
            final Member member,
            final List<OrderInfo> orderInfos,
            final long originalPrice,
            final long usedPoint,
            final long pointToAdd
    ) {
        this(null, member, orderInfos, originalPrice, usedPoint, pointToAdd);
    }
    
    public Order(
            final Long id,
            final Member member,
            final List<OrderInfo> orderInfos,
            final long originalPrice,
            final long usedPoint,
            final long pointToAdd
    ) {
        this.id = id;
        this.member = member;
        this.orderInfos = new OrderInfos(orderInfos);
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }
    
    public Long calculatePayment() {
        return orderInfos.calculateAllProductPriceWithQuantiry() - usedPoint;
    }
}
