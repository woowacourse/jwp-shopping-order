package cart.domain;

public interface DeliveryFeeCalculator {
    
    public int calculate(Member member, OrderItems itemsToOrder);
}
