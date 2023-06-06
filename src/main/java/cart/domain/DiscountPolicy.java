package cart.domain;

public interface DiscountPolicy {

    Money calculateDiscountAmount(Order order);

    boolean canApply(Order order);

    Long getId();

    String getName();

    double getDiscountRate();

    Money getThreshold();

}