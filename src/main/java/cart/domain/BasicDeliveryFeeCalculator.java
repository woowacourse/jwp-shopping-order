package cart.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BasicDeliveryFeeCalculator implements DeliveryFeeCalculator{
    
    public static int BASIC_FEE = 3000;
    
    @Override
    public int calculate(Member member, List<OrderItem> itemsToOrder) {
        int totalPrice = itemsToOrder.stream()
                .mapToInt(orderItem -> orderItem.getProduct().getPrice()* orderItem.getQuantity())
                .sum();
        if(totalPrice > 50000) {
            return 0;
        }
        return BASIC_FEE;
    }
}
