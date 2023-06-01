package cart.domain;

public interface DiscountPolicy {

    Money calculateDiscountAmount(Order order);

    boolean canApply(Order order);

    String getName();

}