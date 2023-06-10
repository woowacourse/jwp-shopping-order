package cart.domain;

import org.springframework.stereotype.Component;

@Component
public class BasicDiscountCalculator implements DiscountCalculator{
    
    public static final int DISCOUNT_PRODUCT_TOTAL_PRICE = 100_000;
    public static final double DISCOUNT_RATE = 0.1;
    
    @Override
    public int calculate(Member member, OrderItems itemsToOrder) {
        int totalPrice = itemsToOrder.getTotalPrice();
        if(totalPrice > DISCOUNT_PRODUCT_TOTAL_PRICE) {
            return (int) (totalPrice * DISCOUNT_RATE) ;
        }
        return 0;
    }
}
