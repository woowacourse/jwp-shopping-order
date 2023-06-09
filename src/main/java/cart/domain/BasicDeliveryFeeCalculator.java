package cart.domain;

import org.springframework.stereotype.Component;

@Component
public class BasicDeliveryFeeCalculator implements DeliveryFeeCalculator{
    
    public static final int DISCOUNT_PRODUCT_TOTAL_PRICE = 50_000;
    public static int BASIC_FEE = 3_000;
    
    @Override
    public int calculate(Member member, OrderItems itemsToOrder) {
        int totalPrice = itemsToOrder.getTotalPrice();
        if(totalPrice > DISCOUNT_PRODUCT_TOTAL_PRICE) {
            return 0;
        }
        return BASIC_FEE;
    }
}
