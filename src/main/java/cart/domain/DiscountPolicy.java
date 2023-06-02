package cart.domain;

public interface DiscountPolicy {

    long getId();

    Money calculateDiscountAmount(Order order);

    boolean canApply(Order order);

    String getName();

}