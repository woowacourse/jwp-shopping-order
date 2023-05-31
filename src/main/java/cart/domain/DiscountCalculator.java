package cart.domain;

import java.util.List;

public interface DiscountCalculator {
    
    int calculate(Member member, List<OrderItem> itemsToOrder);
}
