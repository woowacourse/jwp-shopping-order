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
        
        if(totalPrice > 100000) {
            return (int) (totalPrice * 0.1) ;
        }
        
        return 0;
    }
}
