package cart.order.domain;

import java.util.List;

public class OrderInfos {
    private final List<OrderInfo> orderInfos;
    
    public OrderInfos(final List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }
    
    public Long calculateAllProductPriceWithQuantity() {
        return orderInfos.stream()
                .mapToLong(OrderInfo::calculateProductPriceWithQuantity)
                .sum();
    }
    
    public Long calculatePointToAdd() {
        return orderInfos.stream()
                .mapToLong(OrderInfo::calculatePointToAdd)
                .sum();
    }
    
    public Long calculateAvailablePoint() {
        return orderInfos.stream()
                .mapToLong(OrderInfo::calculateAvailablePoint)
                .sum();
    }
    
    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }
    
    @Override
    public String toString() {
        return "OrderInfos{" +
                "orderInfos=" + orderInfos +
                '}';
    }
}
