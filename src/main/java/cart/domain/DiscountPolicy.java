package cart.domain;

public interface DiscountPolicy {

    Money calculateDiscountAmount(Order order);

}