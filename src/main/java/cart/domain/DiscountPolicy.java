package cart.domain;

public interface DiscountPolicy {

    Long getId();

    Money calculateDiscountAmount(Order order);

    boolean canApply(Order order);

    String getName();

}