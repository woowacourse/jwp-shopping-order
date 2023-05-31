package cart.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CouponDiscountCalculator implements DiscountCalculator{
    
    @Override
    public int calculate(Member member, List<OrderItem> itemsToOrder) {
        int totalPrice = itemsToOrder.stream()
                .mapToInt(orderItem -> orderItem.getProduct().getPrice()* orderItem.getQuantity())
                .sum();
        
        if(totalPrice> 30000) {
            return 3000;
        }
        if (totalPrice > 50000) {
            return 5000;
        }
        
        return 0;
    }
}
