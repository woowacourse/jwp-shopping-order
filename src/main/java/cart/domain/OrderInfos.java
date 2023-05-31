package cart.domain;

import java.util.List;

public class OrderInfos {
    private final List<OrderInfo> orderInfos;
    
    public OrderInfos(final List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }
    
    public Long calculateAllProductPriceWithQuantiry() {
        return orderInfos.stream()
                .mapToLong(OrderInfo::calculateProductPriceWithQuantiry)
                .sum();
    }
}
