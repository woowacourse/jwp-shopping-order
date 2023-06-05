package cart.domain;

import org.springframework.stereotype.Component;

@Component
public class BasicDiscountCalculator implements DiscountCalculator{
    
    @Override
    public int calculate(Member member, OrderItems itemsToOrder) {
        int totalPrice = itemsToOrder.getTotalPrice();
        if(totalPrice > 100000) {
            return (int) (totalPrice * 0.1) ;
        }
        return 0;
    }
}
