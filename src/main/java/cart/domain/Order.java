package cart.domain;

import cart.exception.DiscordException;

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
        validateOriginalPrice(originalPrice);
        validateOriginalPointToAdd(pointToAdd);
        validateUsedPoint(usedPoint);
    }
    
    private void validateOriginalPrice(final long originalPrice) {
        final Long calculatedOriginalPrice = this.orderInfos.calculateAllProductPriceWithQuantity();
        if (calculatedOriginalPrice != originalPrice) {
            final String errorMessage = String.format("클라이언트와 서버의 originalPrice가 다릅니다. Client : %d, Server : %d", originalPrice, calculatedOriginalPrice);
            throw new DiscordException(errorMessage);
        }
    }
    
    private void validateOriginalPointToAdd(final long pointToAdd) {
        final Long calculatedPointToAdd = this.orderInfos.calculatePointToAdd();
        if (calculatedPointToAdd != pointToAdd) {
            final String errorMessage = String.format("클라이언트와 서버의 pointToAdd가 다릅니다. Client : %d, Server : %d", pointToAdd, calculatedPointToAdd);
            throw new DiscordException(errorMessage);
        }
    }
    
    private void validateUsedPoint(final long usedPoint) {
        final Long availablePoint = orderInfos.calculateAvailablePoint();
        if (usedPoint < 0 || usedPoint > availablePoint) {
            throw new DiscordException("사용할 수 있는 적립금은 0 ~ " + availablePoint + " 입니다. 입력된 사용 적립금 : " + usedPoint);
        }
    }
    
    public Long order() {
        member.usePoint(usedPoint);
        member.accumulatePoint(orderInfos.calculatePointToAdd());
        return orderInfos.calculateAllProductPriceWithQuantity() - usedPoint;
    }
    
    public Long getId() {
        return id;
    }
    
    public Member getMember() {
        return member;
    }
    
    public OrderInfos getOrderInfos() {
        return orderInfos;
    }
    
    public Long getOriginalPrice() {
        return originalPrice;
    }
    
    public Long getUsedPoint() {
        return usedPoint;
    }
    
    public Long getPointToAdd() {
        return pointToAdd;
    }
}
