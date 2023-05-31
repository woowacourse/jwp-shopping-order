package cart.domain;

import java.util.List;

public interface DeliveryFeeCalculator {
    
    public int calculate(Member member, List<OrderItem> itemsToOrder);
}
