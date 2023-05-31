package cart.domain;

import java.util.List;

public class OrderInfos {
    private final List<OrderInfo> orderInfos;
    
    public OrderInfos(final List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }
    
    public Long calculateAllProductPriceWithQuantity() {
        return orderInfos.stream()
                .mapToLong(OrderInfo::calculateProductPriceWithQuantiry)
                .sum();
    }
    
    public Long calculatePointToAdd() {
        return orderInfos.stream()
                .mapToLong(OrderInfo::calculatePointToAdd)
                .sum();
    }
}
