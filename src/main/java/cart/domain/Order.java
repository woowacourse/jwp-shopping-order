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
        validateOriginalPrice(originalPrice);
        validateOriginalPointToAdd(pointToAdd);
    }
    
    private void validateOriginalPrice(final long originalPrice) {
        final Long calculatedOriginalPrice = this.orderInfos.calculateAllProductPriceWithQuantity();
        if (calculatedOriginalPrice != originalPrice) {
            final String errorMessage = String.format("클라이언트와 서버의 originalPrice가 다릅니다. Client : %d, Server : %d", originalPrice, calculatedOriginalPrice);
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    private void validateOriginalPointToAdd(final long pointToAdd) {
        final Long calculatedPointToAdd = this.orderInfos.calculatePointToAdd();
        if (calculatedPointToAdd != pointToAdd) {
            final String errorMessage = String.format("클라이언트와 서버의 pointToAdd가 다릅니다. Client : %d, Server : %d", pointToAdd, calculatedPointToAdd);
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    public Long calculatePayment() {
        return orderInfos.calculateAllProductPriceWithQuantity() - usedPoint;
    }
}
