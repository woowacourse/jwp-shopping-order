package cart.domain;

public interface DiscountCalculator {
    
    int calculate(Member member, OrderItems itemsToOrder);
}
